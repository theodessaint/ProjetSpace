import { Component, inject, OnInit, signal, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { MusiqueService } from '../../service/musique-service';

@Component({
  imports: [ ReactiveFormsModule, RouterLink ],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
  encapsulation: ViewEncapsulation.None, //pour avoir root html et body qui fct bein dans css
})
export class LoginPage implements OnInit {
  private authService: AuthService = inject(AuthService);
  private formBuilder: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);
  private musique = inject(MusiqueService);

  protected loginError = signal(false);
  protected userForm!: FormGroup;
  protected usernameCtrl!: FormControl;
  protected passwordCtrl!: FormControl;

  protected passwordErrorMessages = {
    required: 'Le mot de passe est obligatoire'
  };

  ngOnInit(): void {
    this.usernameCtrl = this.formBuilder.control('', Validators.required);
    this.passwordCtrl = this.formBuilder.control('', Validators.required );

    this.userForm = this.formBuilder.group({
      username: this.usernameCtrl,
      password: this.passwordCtrl
    });
  }

  public connecter() {
    this.loginError.set(false);
    this.authService.auth(this.userForm.getRawValue()).subscribe({
      next: resp => {
        if (resp.success == false) {
          this.loginError.set(true);
          return;
        }

        this.authService.token = resp.token;
        this.authService.role = resp.role;
        this.authService.username = this.usernameCtrl.value;

        this.musique.demarrer();
        this.router.navigate([ resp.role === 'ADMIN' ? '/home-admin' : '/home-joueur' ]);
      },
      error: () => this.loginError.set(true)
    });
  }
}
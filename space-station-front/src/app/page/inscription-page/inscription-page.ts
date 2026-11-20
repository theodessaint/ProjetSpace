import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../service/auth-service';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-inscription-page',
  styleUrl: './inscription-page.css',
  templateUrl: './inscription-page.html',
})
export class InscriptionPage implements OnInit{
  private authService: AuthService = inject(AuthService);
  private formBuilder: FormBuilder = inject(FormBuilder);
  private router: Router = inject(Router);

  protected inscriptionError = signal(false);
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

  public inscription() {
    this.inscriptionError.set(false);

    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.authService.inscription(this.userForm.getRawValue()).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: () => this.inscriptionError.set(true)
    });
  }
}

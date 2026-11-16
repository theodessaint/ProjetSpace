import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-home-page-admin',
  imports: [],
  templateUrl: './home-page-admin.html',
  styleUrl: './home-page-admin.css',
  encapsulation: ViewEncapsulation.None,
})
export class HomePageAdmin implements AfterViewInit, OnDestroy {
  private router = inject(Router);

  @ViewChild('astronaute') astro!: ElementRef<HTMLImageElement>;
  private timer?: ReturnType<typeof setInterval>;

  ngAfterViewInit(): void {
    this.deplacer();
    this.timer = setInterval(() => this.deplacer(), 10000);
  }

  ngOnDestroy(): void {
    clearInterval(this.timer);   // on arrête le timer quand on quitte la page
  }

  private deplacer(): void {
    const x = Math.random() * (window.innerWidth - 200);
    const y = Math.random() * (window.innerHeight - 200);
    const angle = Math.random() * 60 - 30;
    this.astro.nativeElement.style.transform = `translate(${x}px, ${y}px) rotate(${angle}deg)`;
  }

  modifierRegles()    { this.router.navigate(['/admin/regles']); }            // page à créer
  partiesEnCours()    { this.router.navigate(['/admin/parties-en-cours']); }  // page à créer
  partiesTerminees()  { this.router.navigate(['/admin/parties-terminees']); } // page à créer

  deconnexion(event: Event): void {
    event.preventDefault();
    // à brancher sur l'authentification
    this.router.navigate(['/login']);
  }
}
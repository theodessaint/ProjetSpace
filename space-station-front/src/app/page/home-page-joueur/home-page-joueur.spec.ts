import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomePageJoueur } from './home-page-joueur';

describe('HomePageJoueur', () => {
  let component: HomePageJoueur;
  let fixture: ComponentFixture<HomePageJoueur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomePageJoueur],
    }).compileComponents();

    fixture = TestBed.createComponent(HomePageJoueur);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

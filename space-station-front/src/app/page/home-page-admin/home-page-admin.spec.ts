import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomePageAdmin } from './home-page-admin';

describe('HomePageAdmin', () => {
  let component: HomePageAdmin;
  let fixture: ComponentFixture<HomePageAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomePageAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(HomePageAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

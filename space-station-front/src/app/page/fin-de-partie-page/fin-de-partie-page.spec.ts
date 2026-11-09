import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FinDePartiePage } from './fin-de-partie-page';

describe('FinDePartiePage', () => {
  let component: FinDePartiePage;
  let fixture: ComponentFixture<FinDePartiePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinDePartiePage],
    }).compileComponents();

    fixture = TestBed.createComponent(FinDePartiePage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

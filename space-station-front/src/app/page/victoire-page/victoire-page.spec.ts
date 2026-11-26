import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VictoirePage } from './victoire-page';

describe('VictoirePage', () => {
  let component: VictoirePage;
  let fixture: ComponentFixture<VictoirePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VictoirePage],
    }).compileComponents();

    fixture = TestBed.createComponent(VictoirePage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

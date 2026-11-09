import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JordanPage } from './jordan-page';

describe('JordanPage', () => {
  let component: JordanPage;
  let fixture: ComponentFixture<JordanPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JordanPage],
    }).compileComponents();

    fixture = TestBed.createComponent(JordanPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

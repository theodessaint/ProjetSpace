import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TuileModule } from './tuile-module';

describe('TuileModule', () => {
  let component: TuileModule;
  let fixture: ComponentFixture<TuileModule>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TuileModule],
    }).compileComponents();

    fixture = TestBed.createComponent(TuileModule);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GameOverPage } from './game-over-page';

describe('GameOverPage', () => {
  let component: GameOverPage;
  let fixture: ComponentFixture<GameOverPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameOverPage],
    }).compileComponents();

    fixture = TestBed.createComponent(GameOverPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

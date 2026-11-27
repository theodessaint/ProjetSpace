import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';
import { parteTermineeGuard } from './parte-terminee-guard';

describe('parteTermineeGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => parteTermineeGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});

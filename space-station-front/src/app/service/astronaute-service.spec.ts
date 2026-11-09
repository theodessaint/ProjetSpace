import { TestBed } from '@angular/core/testing';
import { AstronauteService } from './astronaute-service';

describe('AstronauteService', () => {
  let service: AstronauteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AstronauteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

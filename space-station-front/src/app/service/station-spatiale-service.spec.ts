import { TestBed } from '@angular/core/testing';
import { StationSpatialeService } from './station-spatiale-service';

describe('StationSpatialeService', () => {
  let service: StationSpatialeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StationSpatialeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

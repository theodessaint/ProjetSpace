import { TestBed } from '@angular/core/testing';
import { ModuledansmagameService } from './moduledansmagame-service';

describe('ModuledansmagameService', () => {
  let service: ModuledansmagameService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModuledansmagameService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

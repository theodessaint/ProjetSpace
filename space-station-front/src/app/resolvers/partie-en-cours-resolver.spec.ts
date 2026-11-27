import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { partieEnCoursResolver } from './partie-en-cours-resolver';

describe('partieEnCoursResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => partieEnCoursResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});

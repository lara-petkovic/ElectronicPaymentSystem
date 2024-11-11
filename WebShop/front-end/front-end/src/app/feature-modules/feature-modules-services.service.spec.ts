import { TestBed } from '@angular/core/testing';

import { FeatureModulesServicesService } from './feature-modules-services.service';

describe('FeatureModulesServicesService', () => {
  let service: FeatureModulesServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeatureModulesServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

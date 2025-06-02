import { TestBed } from '@angular/core/testing';

import { PaymentOptionsServiceService } from './payment-options-service.service';

describe('PaymentOptionsServiceService', () => {
  let service: PaymentOptionsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentOptionsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

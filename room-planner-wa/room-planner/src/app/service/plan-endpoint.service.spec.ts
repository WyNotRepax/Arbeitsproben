import { TestBed } from '@angular/core/testing';

import { PlanEndpointService } from './plan-endpoint.service';

describe('PlanEndpointService', () => {
  let service: PlanEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlanEndpointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

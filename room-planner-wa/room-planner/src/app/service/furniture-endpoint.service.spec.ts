import { TestBed } from '@angular/core/testing';

import { FurnitureEndpointService } from './furniture-endpoint.service';

describe('FurnitureEndpointService', () => {
  let service: FurnitureEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FurnitureEndpointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { UserEndpointService } from './user-endpoint.service';

describe('UserEndpointService', () => {
  let service: UserEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserEndpointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

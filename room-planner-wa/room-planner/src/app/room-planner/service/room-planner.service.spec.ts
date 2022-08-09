import { TestBed } from '@angular/core/testing';

import { RoomPlannerService } from './room-planner.service';

describe('RoomPlannerService', () => {
  let service: RoomPlannerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoomPlannerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

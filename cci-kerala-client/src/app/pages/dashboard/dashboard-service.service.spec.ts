import { TestBed } from '@angular/core/testing';

import { DashboardServiceService } from './dashboard-service.service';

describe('DashboardServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DashboardServiceService = TestBed.get(DashboardServiceService);
    expect(service).toBeTruthy();
  });
});

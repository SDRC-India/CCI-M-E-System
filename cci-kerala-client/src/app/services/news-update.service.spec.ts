import { TestBed } from '@angular/core/testing';

import { NewsUpdateService } from './news-update.service';

describe('NewsUpdateService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NewsUpdateService = TestBed.get(NewsUpdateService);
    expect(service).toBeTruthy();
  });
});

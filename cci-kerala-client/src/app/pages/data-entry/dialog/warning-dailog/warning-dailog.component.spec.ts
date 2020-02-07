import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WarningDailogComponent } from './warning-dailog.component';

describe('WarningDailogComponent', () => {
  let component: WarningDailogComponent;
  let fixture: ComponentFixture<WarningDailogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WarningDailogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WarningDailogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

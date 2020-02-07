import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InmatesViewComponent } from './inmates-view.component';

describe('InmatesViewComponent', () => {
  let component: InmatesViewComponent;
  let fixture: ComponentFixture<InmatesViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InmatesViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InmatesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

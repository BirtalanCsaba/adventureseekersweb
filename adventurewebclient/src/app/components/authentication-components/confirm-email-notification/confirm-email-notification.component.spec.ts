import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmEmailNotificationComponent } from './confirm-email-notification.component';

describe('ConfirmEmailNotificationComponent', () => {
  let component: ConfirmEmailNotificationComponent;
  let fixture: ComponentFixture<ConfirmEmailNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmEmailNotificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmEmailNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

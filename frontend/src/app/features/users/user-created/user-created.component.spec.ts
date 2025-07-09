import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreatedComponent } from './user-created.component';

describe('UserCreatedComponent', () => {
  let component: UserCreatedComponent;
  let fixture: ComponentFixture<UserCreatedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCreatedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserCreatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

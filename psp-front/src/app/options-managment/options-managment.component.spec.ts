import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OptionsManagmentComponent } from './options-managment.component';

describe('OptionsManagmentComponent', () => {
  let component: OptionsManagmentComponent;
  let fixture: ComponentFixture<OptionsManagmentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OptionsManagmentComponent]
    });
    fixture = TestBed.createComponent(OptionsManagmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

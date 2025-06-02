import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PspFormComponent } from './psp-form.component';

describe('PspFormComponent', () => {
  let component: PspFormComponent;
  let fixture: ComponentFixture<PspFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PspFormComponent]
    });
    fixture = TestBed.createComponent(PspFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

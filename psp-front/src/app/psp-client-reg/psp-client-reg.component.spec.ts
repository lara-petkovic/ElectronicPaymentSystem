import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PspClientRegComponent } from './psp-client-reg.component';

describe('PspClientRegComponent', () => {
  let component: PspClientRegComponent;
  let fixture: ComponentFixture<PspClientRegComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PspClientRegComponent]
    });
    fixture = TestBed.createComponent(PspClientRegComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

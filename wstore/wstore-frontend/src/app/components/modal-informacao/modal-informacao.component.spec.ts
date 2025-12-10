import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalInformacaoComponent } from './modal-informacao.component';

describe('ModalInformacaoComponent', () => {
  let component: ModalInformacaoComponent;
  let fixture: ComponentFixture<ModalInformacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalInformacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalInformacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

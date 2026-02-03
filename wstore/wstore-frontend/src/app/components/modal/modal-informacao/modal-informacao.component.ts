import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-informacao',
  templateUrl: './modal-informacao.component.html',
  styleUrl: './modal-informacao.component.scss'
})
export class ModalInformacaoComponent {

    readonly data = inject<{ message?: string }>(MAT_DIALOG_DATA);
  
}

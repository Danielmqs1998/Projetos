import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-confirmacao',
  templateUrl: './modal-confirmacao.component.html',
  styleUrl: './modal-confirmacao.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ModalConfirmacaoComponent {

  readonly dialogRef = inject(MatDialogRef<ModalConfirmacaoComponent>);
  readonly data = inject<{ message?: string }>(MAT_DIALOG_DATA);

  confirmar() {
    this.dialogRef.close(true);
  }
}
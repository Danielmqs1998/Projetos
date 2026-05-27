import { Component, OnInit, inject, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ModalConfirmacaoComponent } from '../../components/modal/modal-confirmacao/modal-confirmacao.component';
import { CarrinhoService } from '../../services/carrinho.service';
import { ItemCarrinho } from '../../models/itemCarrinho';

/**
 * Refatorado:
 *  - dados ficticios de elementos quimicos removidos
 *  - tabela agora exibe os itens reais do CarrinhoService
 *  - finalizacao limpa o carrinho apos confirmacao
 */
@Component({
  selector: 'app-compra',
  templateUrl: './compra.component.html',
  styleUrl: './compra.component.css'
})
export class CompraComponent implements OnInit {

  displayedColumns: string[] = ['codigo', 'quantidade'];
  itens: ItemCarrinho[] = [];

  readonly dialog = inject(MatDialog);
  readonly router = inject(Router);
  readonly carrinhoService = inject(CarrinhoService);
  readonly compraFinalizada = signal(false);

  ngOnInit(): void {
    this.itens = this.carrinhoService.itens;
  }

  abrirModalConfirmacao(): void {
    if (this.itens.length === 0) return;

    const dialogRef = this.dialog.open(ModalConfirmacaoComponent, {
      data: { message: 'Deseja finalizar a compra?' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.carrinhoService.limpar();
        this.compraFinalizada.set(true);
        this.router.navigate(['/home']);
      }
    });
  }
}

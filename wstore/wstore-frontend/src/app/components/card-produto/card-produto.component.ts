import { Component, Input } from '@angular/core';
import { Produto } from '../../models/produto';

@Component({
  selector: 'app-card-produto',
  templateUrl: './card-produto.component.html',
  styleUrl: './card-produto.component.css'
})
export class CardProdutoComponent {

      @Input() produto?: Produto;
  
}

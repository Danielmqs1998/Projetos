import { Component } from '@angular/core';
import { Produto } from '../../../models/product';

@Component({
  selector: 'app-detalhes-produto',
  templateUrl: './detalhes-produto.component.html',
  styleUrl: './detalhes-produto.component.css'
})
export class DetalhesProdutoComponent {

    produto: Produto | undefined;

    constructor(){}

    teste(){

    }
}

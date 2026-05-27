import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Produto } from '../../../models/produto';
import { ProductService } from '../../../services/product.service';
import { CarrinhoService } from '../../../services/carrinho.service';

/**
 * Refatorado: carrega o produto pelo codigo da rota e
 * expoe acao de adicionar ao carrinho.
 */
@Component({
  selector: 'app-detalhes-produto',
  templateUrl: './detalhes-produto.component.html',
  styleUrl: './detalhes-produto.component.css'
})
export class DetalhesProdutoComponent implements OnInit {

  produto: Produto | undefined;
  carregando: boolean = false;
  erro: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private carrinhoService: CarrinhoService
  ) {}

  ngOnInit(): void {
    const codigo = this.route.snapshot.paramMap.get('codigo');
    if (!codigo) return;

    this.carregando = true;
    this.productService.consultarProduto(codigo).subscribe({
      next: (produto) => {
        this.produto = produto;
        this.carregando = false;
      },
      error: () => {
        this.erro = 'Produto não encontrado.';
        this.carregando = false;
      }
    });
  }

  adicionarAoCarrinho(): void {
    if (this.produto?.codigo) {
      this.carrinhoService.adicionar(this.produto.codigo, 1);
    }
  }
}

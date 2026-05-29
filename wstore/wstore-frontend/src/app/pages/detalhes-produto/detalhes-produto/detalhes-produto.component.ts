import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Produto } from '../../../models/produto';
import { ProductService } from '../../../services/product.service';
import { CarrinhoService } from '../../../services/carrinho.service';
import { FiltroService } from '../../../services/filtro.service';
import { FiltroCategoriaProduto } from '../../../enums/filtroCategoriaProduto';

@Component({
  selector: 'app-detalhes-produto',
  templateUrl: './detalhes-produto.component.html',
  styleUrl: './detalhes-produto.component.css'
})
export class DetalhesProdutoComponent implements OnInit, OnDestroy {

  produto: Produto | undefined;
  produtosRelacionados: Produto[] = [];
  carregando = true;
  erro: string | null = null;

  // Quantidade selecionada pelo usuario
  quantidade = 1;
  opcoes = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

  // Indice da imagem ativa no carousel (preparado para multiplas imagens)
  imagemAtiva = 0;

  // Feedback de acao do carrinho
  adicionado = false;

  private destroy$ = new Subject<void>();

  readonly categoriaLabel: Record<number, string> = {
    0: 'Todos',
    1: 'Eletrônicos',
    2: 'Acessórios',
    3: 'Mobília',
    4: 'Moda',
    5: 'Ferramentas',
    6: 'Lazer'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private carrinhoService: CarrinhoService,
    private filtroService: FiltroService
  ) {}

  ngOnInit(): void {
    // Recarrega a pagina ao navegar entre produtos relacionados
    this.route.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe(params => {
        const codigo = params.get('codigo');
        if (codigo) this.carregarProduto(codigo);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private carregarProduto(codigo: string): void {
    this.carregando = true;
    this.erro = null;
    this.adicionado = false;
    this.imagemAtiva = 0;

    this.productService.consultarProduto(codigo).subscribe({
      next: (produto) => {
        this.produto = produto;
        this.carregando = false;
        this.carregarRelacionados(produto.categoria);
      },
      error: () => {
        this.erro = 'Produto não encontrado ou indisponível.';
        this.carregando = false;
      }
    });
  }

  private carregarRelacionados(categoria: number): void {
    this.filtroService.update({ categoria: categoria as FiltroCategoriaProduto });
    this.productService.consultarProdutos(0).subscribe({
      next: (page) => {
        // Exclui o produto atual da lista de relacionados
        this.produtosRelacionados = page.content
          .filter(p => p.codigo !== this.produto?.codigo)
          .slice(0, 4);
      },
      error: () => { this.produtosRelacionados = []; }
    });
  }

  get precoParcelado(): number {
    return (this.produto?.preco ?? 0) / 10;
  }

  get precoComDesconto(): number | null {
    const p = this.produto;
    if (!p) return null;
    // Usa precoPromocional se existir (campo do backend nao mapeado no frontend ainda)
    return (p as any).precoPromocional > 0 ? (p as any).precoPromocional : null;
  }

  get percentualDesconto(): number {
    const p = this.produto;
    if (!p || !(p as any).precoPromocional) return 0;
    return Math.round((1 - (p as any).precoPromocional / p.preco) * 100);
  }

  adicionarAoCarrinho(): void {
    if (!this.produto?.codigo) return;
    this.carrinhoService.adicionar(this.produto.codigo, this.quantidade);
    this.adicionado = true;
    setTimeout(() => this.adicionado = false, 2500);
  }

  comprarAgora(): void {
    if (!this.produto?.codigo) return;
    this.carrinhoService.adicionar(this.produto.codigo, this.quantidade);
    this.router.navigate(['/compra']);
  }

  navegarParaRelacionado(codigo: string): void {
    this.router.navigate(['/detalhes-produto', codigo]);
  }

  voltarParaListagem(): void {
    this.router.navigate(['/home']);
  }
}

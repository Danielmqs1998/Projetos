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
  quantidade = 1;
  opcoes = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  imagemAtiva = 0;
  adicionado = false;

  // Carrossel de relacionados
  indiceCarrossel = 0;
  readonly VISIVEIS = 4; // quantos cards aparecem por vez

  private destroy$ = new Subject<void>();

  readonly categoriaLabel: Record<number, string> = {
    0: 'Todos', 1: 'Eletrônicos', 2: 'Acessórios',
    3: 'Mobília', 4: 'Moda', 5: 'Ferramentas', 6: 'Lazer'
  };

  readonly condicaoLabel: Record<number, string> = {
    0: 'Padrão', 1: 'Promoção', 2: 'Frete grátis', 3: 'Entrega rápida'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private carrinhoService: CarrinhoService,
    private filtroService: FiltroService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe(params => {
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
    this.indiceCarrossel = 0;

    this.productService.consultarProduto(codigo).subscribe({
      next: (produto: Produto) => {
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
        this.produtosRelacionados = page.content
          .filter(p => p.codigo !== this.produto?.codigo)
          .slice(0, 8); // até 8 para o carrossel ter para onde deslizar
      },
      error: () => { this.produtosRelacionados = []; }
    });
  }

  // ── Carrossel ──────────────────────────────────────────────────
  get relacionadosVisiveis(): Produto[] {
    return this.produtosRelacionados.slice(
      this.indiceCarrossel,
      this.indiceCarrossel + this.VISIVEIS
    );
  }

  get podeDeslizarEsquerda(): boolean { return this.indiceCarrossel > 0; }
  get podeDeslizarDireita(): boolean {
    return this.indiceCarrossel + this.VISIVEIS < this.produtosRelacionados.length;
  }

  deslizarEsquerda(): void { if (this.podeDeslizarEsquerda) this.indiceCarrossel--; }
  deslizarDireita(): void  { if (this.podeDeslizarDireita)  this.indiceCarrossel++; }

  // ── Preços ────────────────────────────────────────────────────
  get precoParcelado(): number { return (this.produto?.preco ?? 0) / 10; }

  get precoComDesconto(): number | null {
    const p = this.produto as any;
    return p?.precoPromocional > 0 ? p.precoPromocional : null;
  }

  get percentualDesconto(): number {
    const p = this.produto as any;
    if (!p?.precoPromocional) return 0;
    return Math.round((1 - p.precoPromocional / p.preco) * 100);
  }

  // ── Especificações extras ─────────────────────────────────────
  get fornecedorLabel(): string {
    const f = this.produto?.fornecedor ?? 0;
    const map: Record<number, string> = {
      0: 'Não informado', 1: 'Nacional', 2: 'Importado', 3: 'Fabricante próprio'
    };
    return map[f] ?? `Fornecedor ${f}`;
  }

  // ── Ações ─────────────────────────────────────────────────────
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

  navegarParaRelacionado(codigo: string): void { this.router.navigate(['/detalhes-produto', codigo]); }
  voltarParaListagem(): void { this.router.navigate(['/home']); }
}

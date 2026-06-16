import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, skip, takeUntil } from 'rxjs';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { Produto } from '../../models/produto';
import { Page } from '../../models/rest/page';
import { UsuarioLoja } from '../../models/rest/usuarioLoja';
import { FiltroCategoriaProduto } from '../../enums/filtroCategoriaProduto';
import { FiltroCondicaoProduto } from '../../enums/filtroCondicaoProduto';
import { FiltroEstadoProduto } from '../../enums/filtroEstadoProduto';
import { AuthService } from '../../services/auth.service';
import { FiltroService } from '../../services/filtro.service';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {

  public produtos: Produto[] = [];
  public paginaProdutos?: Page<Produto> | null;
  public usuarioLoja?: UsuarioLoja | null;
  public ultimaPagina: boolean = false;
  public totalItems: number = 0;
  public paginaAtual: number = 1;
  public previousLabel: string = 'Ant.';
  public nextLabel: string = 'Próx.';
  public precoMaximo?: number | null;
  public precoMinimo?: number | null;
  public estado: FiltroEstadoProduto = FiltroEstadoProduto.Todos;
  public estados = Object.values(FiltroEstadoProduto);
  public condicoes: FiltroCondicaoProduto[] = [];
  public categoria: FiltroCategoriaProduto = FiltroCategoriaProduto.Todas;
  public FiltroCondicaoProduto = FiltroCondicaoProduto;
  public carregando: boolean = false;

  private destroy$ = new Subject<void>();

  constructor(
    private productService: ProductService,
    private authService: AuthService,
    private filtroService: FiltroService
  ) {}

  ngOnInit(): void {
    this.authService.carregarUsuario().subscribe();
    this.consultaProdutos(1);

    this.filtroService.filtros$
      .pipe(skip(1), takeUntil(this.destroy$))
      .subscribe(() => {
        this.paginaAtual = 1;
        this.consultaProdutos(1);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  consultaProdutos(pagina: number): void {
    this.paginaAtual = pagina;
    this.carregando = true;
    this.productService.consultarProdutos(this.paginaAtual - 1).subscribe({
      next: (page) => {
        this.produtos = page.content;
        this.ultimaPagina = page.last;
        this.totalItems = page.totalElements;
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        alert('Falha ao listar produtos, consulte o suporte técnico.');
      }
    });
  }

  onFiltroCondicoesChange(event: MatCheckboxChange): void {
    const value = event.source.value as FiltroCondicaoProduto;
    if (event.checked) {
      this.condicoes = [...this.condicoes, value];
    } else {
      this.condicoes = this.condicoes.filter(v => v !== value);
    }
  }

  aplicarFiltros(): void {
    this.filtroService.update({
      precoMaximo: this.precoMaximo ?? 0,
      precoMinimo: this.precoMinimo ?? 0,
      estado: this.estado,
      condicoes: this.condicoes
    });
  }
}

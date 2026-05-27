import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { FiltroCategoriaProduto } from '../../enums/filtroCategoriaProduto';
import { UsuarioLoja } from '../../models/rest/usuarioLoja';
import { AuthService } from '../../services/auth.service';
import { CarrinhoService } from '../../services/carrinho.service';
import { FiltroService } from '../../services/filtro.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {

  public usuarioLoja: UsuarioLoja | null = null;
  public paginaPrincipal: boolean = false;
  public textoPesquisa: string = '';
  public FiltroCategoriaProduto = FiltroCategoriaProduto;
  public categoriaSelecionada = FiltroCategoriaProduto.Todas;
  public totalItensCarrinho: number = 0;

  // Subject para cancelar todas as subscriptions no destroy
  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private authService: AuthService,
    private filtroService: FiltroService,
    public carrinhoService: CarrinhoService
  ) {}

  ngOnInit(): void {
    // Subscription ao usuario autenticado
    this.authService.usuario$()
      .pipe(takeUntil(this.destroy$))
      .subscribe(usuario => this.usuarioLoja = usuario);

    // Subscription ao estado do carrinho
    this.carrinhoService.itens$
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.totalItensCarrinho = this.carrinhoService.totalItens);

    // Subscription ao router para detectar pagina principal
    this.router.events
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.paginaPrincipal = this.router.url === '/home');
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onFiltroCategoriaChange(filtroCategoriaProduto: FiltroCategoriaProduto): void {
    this.categoriaSelecionada =
      this.filtroService.value.categoria === filtroCategoriaProduto
        ? FiltroCategoriaProduto.Todas
        : filtroCategoriaProduto;
    this.filtroService.update({ categoria: this.categoriaSelecionada });
  }

  pesquisarProduto(): void {
    this.filtroService.update({ descricao: this.textoPesquisa });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

import { Component } from '@angular/core';
import { Produto } from '../../models/produto';
import { UsuarioLoja } from '../../models/rest/usuarioLoja';
import { AuthService } from '../../services/auth.service';
import { ProductService } from '../../services/product.service';
import { Page } from '../../models/rest/page';
import { FiltroEstadoProduto } from '../../enums/filtroEstadoProduto';
import { FiltroCondicaoProduto } from '../../enums/filtroCondicaoProduto';
import { FiltroCategoriaProduto } from '../../enums/filtroCategoriaProduto';
import { FiltroService } from '../../services/filtro.service';
import { skip, Subject, takeUntil } from 'rxjs';
import { MatCheckboxChange } from '@angular/material/checkbox';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent {

    public produtos: Produto[] = [];
    public paginaProdutos?: Page<Produto> | null;
    public usuarioLoja?: UsuarioLoja | null;
    public ultimaPagina: boolean = false;
    public totalItems: number = 0;
    public paginaAtual: number = 1;
    public previousLabel: string = "Ant.";
    public nextLabel: string = "Próx.";
    public precoMaximo?: number | null; 
    public precoMinimo?: number | null;
    public estado: FiltroEstadoProduto = FiltroEstadoProduto.Novo;
    public estados = Object.values(FiltroEstadoProduto);
    public condicoes: FiltroCondicaoProduto[] = [];
    public categoria: FiltroCategoriaProduto = FiltroCategoriaProduto.Todas;
    public FiltroCondicaoProduto = FiltroCondicaoProduto;
    private destroy$ = new Subject<void>();

    constructor(
        private productService: ProductService, 
        private authService: AuthService,
        private filtroService: FiltroService) { 

    }

    ngOnInit(): void {
        this.authService.carregarUsuario().subscribe();
        this.consultaProdutos(this.paginaAtual - 1);
        this.filtroService.filtros$
            .pipe(
                skip(1), // para nao carregar os filtros ao iniciar
                takeUntil(this.destroy$)
            )
            .subscribe(() => { 
                this.consultaProdutos(this.paginaAtual); 
            });
    }

    consultaProdutos(event: number): void{
        this.paginaAtual = event;
        this.productService.consultarProdutos(this.paginaAtual - 1).subscribe({
            next: (produtos) => {
                this.produtos = produtos.content;
                this.ultimaPagina = produtos.last;
                this.totalItems = produtos.totalElements;
            }, 
            error: () => {
                alert("Falha ao listar produtos, consulte o suporte técnico.")
            }
        })
    }

    onFiltroCondicoesChange(event: MatCheckboxChange) {
        const value = event.source.value as FiltroCondicaoProduto;
        if (event.checked) {
            this.condicoes.push(value);
        } else {
            this.condicoes = this.condicoes.filter(v => v !== value);
        }
    }

    aplicarFiltros(){
        this.filtroService.update({ 
            precoMaximo: this.precoMaximo == undefined || this.precoMaximo == null ? 0.0 : this.precoMaximo, 
            precoMinimo: this.precoMinimo == undefined || this.precoMinimo == null ? 0.0 : this.precoMinimo, 
            estado: this.estado,
            condicoes: this.condicoes 
        })
    }

}

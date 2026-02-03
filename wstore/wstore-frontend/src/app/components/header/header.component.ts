import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ItemCarrinho } from '../../models/itemCarrinho';
import { UsuarioLoja } from '../../models/rest/usuarioLoja';
import { AuthService } from '../../services/auth.service';
import { FiltroService } from '../../services/filtro.service';
import { FiltroCategoriaProduto } from '../../enums/filtroCategoriaProduto';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent {

    public itensCarrinho: ItemCarrinho[] = [];
    public usuarioLoja: UsuarioLoja | null = null;
    public paginaPrincipal: Boolean = false;
    public textoPesquisa: string = "";
    public FiltroCategoriaProduto = FiltroCategoriaProduto;
    public categoriaSelecionada = FiltroCategoriaProduto.Todas;

    constructor(private router: Router, private authService: AuthService, private filtroService: FiltroService) {
        this.router.events.subscribe(() => {
            this.paginaPrincipal = this.router.url === '/home';
        });   
    }

    ngOnInit(): void {
        this.authService.usuario$().subscribe(usuarioLoja => {
            this.usuarioLoja = usuarioLoja;
        });
    }
    
    onFiltroCategoriaChange(filtroCategoriaProduto: FiltroCategoriaProduto) {
        this.categoriaSelecionada = this.filtroService.value.categoria === filtroCategoriaProduto ? FiltroCategoriaProduto.Todas : filtroCategoriaProduto;
        this.filtroService.update({ categoria: this.categoriaSelecionada});
    }

    pesquisarProduto(){
        this.filtroService.update({ descricao: this.textoPesquisa });
    }

    adicionarItemCarrinho(codigoProduto: string, quantidade: number): void {
        this.itensCarrinho.push({ codigoProduto, quantidade });
    }

}

import { Component, Input } from '@angular/core';
import { Produto } from '../../models/product';
import { UsuarioLoja } from '../../models/rest/usuarioLoja';
import { AuthService } from '../../services/auth.service';
import { ProductService } from '../../services/product.service';
import { Page } from '../../models/rest/page';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent {

    produtos: Produto[] = [];
    paginaProdutos?: Page<Produto> | null;
    usuarioLoja?: UsuarioLoja | null;
    ultimaPagina: boolean = false;
    totalItems: number = 0;
    paginaAtual: number = 1;
    previousLabel: string = "Anterior";
    nextLabel: string = "Próximo"

    constructor(private productService: ProductService, private authService: AuthService) { }

    ngOnInit(): void {
        this.authService.carregarUsuario().subscribe();
        this.consultaProdutos(this.paginaAtual - 1);
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
                
            }
        })
    }

    teste(): void{

    }
}

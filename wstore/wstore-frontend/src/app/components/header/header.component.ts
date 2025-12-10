import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ItemCarrinho } from '../../models/itemCarrinho';
import { UsuarioLoja } from '../../models/rest/usuarioLoja';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent {

    itensCarrinho: ItemCarrinho[] = [];
    usuarioLoja: UsuarioLoja | null = null;
    paginaPrincipal: Boolean = false;

    constructor(private router: Router, private authService: AuthService) {
        this.router.events.subscribe(() => {
            this.paginaPrincipal = this.router.url === '/home';
        });
    }

    ngOnInit(): void {
        this.authService.usuario$().subscribe(usuarioLoja => {
            this.usuarioLoja = usuarioLoja;
        });
    }

    adicionarItemCarrinho(codigoProduto: string, quantidade: number): void {
        this.itensCarrinho.push({ codigoProduto, quantidade });
    }

}

import { Injectable } from '@angular/core';
import { Produto } from '../models/produto';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthRequest } from '../models/rest/authRequest';
import { Page } from '../models/rest/page';
import { FiltroService } from './filtro.service';
import { Filtros } from '../models/filtros';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private http: HttpClient;
  private filtroService: FiltroService;
  private filtros?: Filtros;

  constructor(http:HttpClient, filtroService: FiltroService) { 
      this.http = http;
      this.filtroService = filtroService;
  }

  login(username: string, password: string) {
      const payload: AuthRequest = { username, password };
      return this.http.post('http://localhost:3000/auth/login', payload, { withCredentials: true}); 
  }

  consultarProdutos(paginaAtual: number): Observable<Page<Produto>>{
      this.filtros = this.filtroService.value;
      return this.http.get<Page<Produto>>('http://localhost:3000/produtos/listar?size=16' 
         + '&page=' + paginaAtual
         + '&precoMaximo=' + this.filtros.precoMaximo
         + '&precoMinimo=' + this.filtros.precoMinimo
         + '&descricao=' + this.filtros.descricao
         + '&estado=' + this.filtros.estado
         + '&condicoes=' + this.filtros.condicoes.join(";")
         + '&categoria=' + this.filtros.categoria,
        { withCredentials: true });

  }

}

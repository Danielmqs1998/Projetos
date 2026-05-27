import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/rest/page';
import { Produto } from '../models/produto';
import { FiltroService } from './filtro.service';
import { Filtros } from '../models/filtros';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private filtroService: FiltroService) {}

  consultarProdutos(paginaAtual: number): Observable<Page<Produto>> {
    const filtros: Filtros = this.filtroService.value;

    // HttpParams garante encoding correto e evita concatenacao manual de strings
    let params = new HttpParams()
      .set('size', '16')
      .set('page', String(paginaAtual));

    if (filtros.precoMaximo && filtros.precoMaximo > 0) {
      params = params.set('precoMaximo', String(filtros.precoMaximo));
    }
    if (filtros.precoMinimo && filtros.precoMinimo > 0) {
      params = params.set('precoMinimo', String(filtros.precoMinimo));
    }
    if (filtros.descricao && filtros.descricao.trim().length > 0) {
      params = params.set('descricao', filtros.descricao.trim());
    }
    if (filtros.estado) {
      params = params.set('estado', filtros.estado);
    }
    if (filtros.condicoes && filtros.condicoes.length > 0) {
      params = params.set('condicoes', filtros.condicoes.join(';'));
    }
    if (filtros.categoria) {
      params = params.set('categoria', filtros.categoria);
    }

    return this.http.get<Page<Produto>>(`${this.apiUrl}/produtos/listar`, {
      params,
      withCredentials: true
    });
  }

  consultarProduto(codigo: string): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/produtos/consultar`, {
      params: new HttpParams().set('codigo', codigo),
      withCredentials: true
    });
  }
}

import { Injectable } from '@angular/core';
import { Produto } from '../models/product';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthRequest } from '../models/rest/authRequest';
import { Page } from '../models/rest/page';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private http: HttpClient;

  constructor(http:HttpClient) { 
      this.http = http;
  }

  login(username: string, password: string) {
      const payload: AuthRequest = { username, password };
      return this.http.post('http://localhost:3000/auth/login', payload, { withCredentials: true}); 
  }
  
  consultarProdutos(paginaAtual: number): Observable<Page<Produto>>{
      return this.http.get<Page<Produto>>('http://localhost:3000/produtos/listar?size=16&page=' + paginaAtual, { withCredentials: true });
  }

}

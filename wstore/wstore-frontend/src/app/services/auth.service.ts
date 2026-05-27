import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, of, switchMap, tap, throwError } from 'rxjs';
import { AuthResponse } from '../models/rest/authResponse';
import { UsuarioLoja } from '../models/rest/usuarioLoja';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = environment.apiUrl;
  private usuarioSubject = new BehaviorSubject<UsuarioLoja | null>(null);

  constructor(private http: HttpClient) {}

  setUsuario(usuario: UsuarioLoja) {
    this.usuarioSubject.next(usuario);
  }

  getUsuario(): UsuarioLoja | null {
    return this.usuarioSubject.value;
  }

  carregarUsuario(): Observable<UsuarioLoja | null> {
    return this.http
      .get<UsuarioLoja>(`${this.apiUrl}/usuarios/dados_usuario`, { withCredentials: true })
      .pipe(
        tap(usuario => this.usuarioSubject.next(usuario)),
        catchError(() => {
          this.usuarioSubject.next(null);
          return of(null);
        })
      );
  }

  usuario$(): Observable<UsuarioLoja | null> {
    return this.usuarioSubject.asObservable();
  }

  isLogado(): boolean {
    return !!this.usuarioSubject.value;
  }

  logout(): void {
    this.http
      .post(`${this.apiUrl}/auth/logout`, {}, { withCredentials: true })
      .pipe(catchError(() => of(null)))
      .subscribe(() => this.usuarioSubject.next(null));
  }

  efetuarLogin(login: string, senha: string): Observable<UsuarioLoja> {
    return this.http
      .post<AuthResponse>(
        `${this.apiUrl}/auth/login`,
        { username: login, password: senha },
        { withCredentials: true }
      )
      .pipe(
        switchMap(res => {
          if (res.autenticado) {
            return this.http.get<UsuarioLoja>(
              `${this.apiUrl}/usuarios/dados_usuario`,
              { withCredentials: true }
            );
          }
          return throwError(() => new Error('Não autenticado'));
        }),
        tap(usuario => this.setUsuario(usuario))
      );
  }
}

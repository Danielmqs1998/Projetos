import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Impede acesso a rotas protegidas por usuarios nao autenticados,
 * redirecionando para /login.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isLogado()) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}

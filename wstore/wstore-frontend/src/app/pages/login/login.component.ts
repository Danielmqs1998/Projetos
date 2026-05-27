import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';
import { ModalInformacaoComponent } from '../../components/modal/modal-informacao/modal-informacao.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  login: string = '';
  senha: string = '';
  carregando: boolean = false;

  private readonly dialog = inject(MatDialog);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  efetuarLogin(): void {
    if (!this.login.trim() || !this.senha.trim()) return;

    this.carregando = true;
    this.authService.efetuarLogin(this.login, this.senha).subscribe({
      next: () => {
        this.carregando = false;
        // Corrigido: usava dialog.afterAllClosed (global, causava memory leak).
        // Agora usa dialogRef.afterClosed() (escopo da instancia).
        const ref = this.dialog.open(ModalInformacaoComponent, {
          data: { message: 'Login efetuado com sucesso!' }
        });
        ref.afterClosed().subscribe(() => this.router.navigate(['/home']));
      },
      error: () => {
        this.carregando = false;
        this.dialog.open(ModalInformacaoComponent, {
          data: { message: 'Usuário ou senha inválidos.' }
        });
      }
    });
  }
}

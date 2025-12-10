import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ModalInformacaoComponent } from '../../components/modal-informacao/modal-informacao.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  login: string = "";
  senha: string = "";

  private readonly dialog = inject(MatDialog);
  private authService: AuthService;
  private router: Router;

  constructor(authService: AuthService, router: Router) {
    this.authService = authService;
    this.router = router;
  }

  abrirModalInformacao(message: String, autenticado: boolean): void {

    this.dialog.open(ModalInformacaoComponent, {
      data: { message: message },
    });

    this.dialog.afterAllClosed.subscribe(() => {
      if (autenticado) {
        this.router.navigate(['/home'])
      }
    })

  }

  efetuarLogin() {
    this.authService.efetuarLogin(this.login, this.senha).subscribe({
      next: usuario => {
        this.abrirModalInformacao("Login efetuado com sucesso!", true)
      },
      error: (err) => {
        this.abrirModalInformacao("Falha ao efetuar login!", false)
      }
    });
  }

}

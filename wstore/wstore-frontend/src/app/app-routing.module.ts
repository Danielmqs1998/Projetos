import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AboutComponent } from './pages/about/about.component';
import { LoginComponent } from './pages/login/login.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { CompraComponent } from './pages/compra/compra.component';
import { DetalhesProdutoComponent } from './pages/detalhes-produto/detalhes-produto/detalhes-produto.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: 'login',    component: LoginComponent },
  { path: 'cadastro', component: CadastroComponent },
  { path: 'home',     component: HomeComponent },
  { path: 'about/:id', component: AboutComponent },
  { path: 'detalhes-produto/:codigo', component: DetalhesProdutoComponent },
  // Rota protegida: exige autenticacao
  { path: 'compra', component: CompraComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

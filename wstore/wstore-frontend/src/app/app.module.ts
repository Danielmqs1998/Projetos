import { LOCALE_ID, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 
import { provideHttpClient } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { ModalConfirmacaoComponent } from './components/modal-confirmacao/modal-confirmacao.component';
import { AboutComponent } from './pages/about/about.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { CompraComponent } from './pages/compra/compra.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { ProductService } from './services/product.service';
import { ModalInformacaoComponent } from './components/modal-informacao/modal-informacao.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { CardProdutoComponent } from './components/card-produto/card-produto.component';
import localePt from '@angular/common/locales/pt';
import { registerLocaleData } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { DetalhesProdutoComponent } from './pages/detalhes-produto/detalhes-produto/detalhes-produto.component';
import { DivisorComponent } from './components/divisor/divisor.component';
import { RatingComponent } from './components/rating/rating.component';
import { MatOption } from '@angular/material/core';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatSelect } from '@angular/material/select';

registerLocaleData(localePt);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    LoginComponent,
    HeaderComponent,
    FooterComponent,
    CadastroComponent,
    CompraComponent,
    ModalConfirmacaoComponent,
    ModalInformacaoComponent,
    CardProdutoComponent,
    DetalhesProdutoComponent,
    DivisorComponent,
    RatingComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule, // Adicione este módulo
    FormsModule,
    AppRoutingModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatCheckboxModule,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatOption,
    MatLabel,
    MatSelect,
    MatFormField,
    NgxPaginationModule
  ],
  providers: [
    ProductService,
    provideHttpClient(), provideAnimationsAsync(),
    { provide: LOCALE_ID, useValue: 'pt' }
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }

import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Filtros } from '../models/filtros';
import { FiltroEstadoProduto } from '../enums/filtroEstadoProduto';
import { FiltroCategoriaProduto } from '../enums/filtroCategoriaProduto';

@Injectable({
  providedIn: 'root'
})
export class FiltroService {

  private filtrosSubject = new BehaviorSubject<Filtros>({
    precoMaximo:  0,
    precoMinimo:  0,
    categoria:    FiltroCategoriaProduto.Todas,
    condicoes:    [],
    descricao:    '',
    // Inicializa sem filtro de estado para retornar todos os produtos
    estado:       FiltroEstadoProduto.Todos
  });

  filtros$ = this.filtrosSubject.asObservable();

  update(filtro: Partial<Filtros>) {
    this.filtrosSubject.next({
      ...this.filtrosSubject.value,
      ...filtro
    });
  }

  get value(): Filtros {
    return this.filtrosSubject.value;
  }
}

import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ItemCarrinho } from '../models/itemCarrinho';

/**
 * Gerencia o estado do carrinho de compras em toda a aplicacao.
 * Substitui o array local no HeaderComponent, que se perdia ao navegar.
 */
@Injectable({
  providedIn: 'root'
})
export class CarrinhoService {

  private itensSubject = new BehaviorSubject<ItemCarrinho[]>([]);

  itens$: Observable<ItemCarrinho[]> = this.itensSubject.asObservable();

  get itens(): ItemCarrinho[] {
    return this.itensSubject.value;
  }

  get totalItens(): number {
    return this.itens.reduce((acc, item) => acc + item.quantidade, 0);
  }

  adicionar(codigoProduto: string, quantidade: number = 1): void {
    const lista = [...this.itens];
    const existente = lista.find(i => i.codigoProduto === codigoProduto);

    if (existente) {
      existente.quantidade += quantidade;
    } else {
      lista.push({ codigoProduto, quantidade });
    }

    this.itensSubject.next(lista);
  }

  remover(codigoProduto: string): void {
    this.itensSubject.next(
      this.itens.filter(i => i.codigoProduto !== codigoProduto)
    );
  }

  limpar(): void {
    this.itensSubject.next([]);
  }
}

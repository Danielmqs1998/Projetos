import { Component, Input } from '@angular/core';
import { Produto } from '../../models/produto';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.css'
})
export class RatingComponent {

  @Input() produto?: Produto;

  /** Converte classificacao (0–5) para inteiro seguro. */
  get rating(): number {
    const r = Math.round(this.produto?.classificacao ?? 0);
    return Math.min(5, Math.max(0, r));
  }
}

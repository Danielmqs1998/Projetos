import { Component, Input } from '@angular/core';
import { Produto } from '../../models/produto';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.css'
})
export class RatingComponent {

  /** Tipagem correta: era 'any', agora usa o modelo Produto */
  @Input() produto?: Produto;

}

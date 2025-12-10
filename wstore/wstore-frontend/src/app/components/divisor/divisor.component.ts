import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-divisor',
  templateUrl: './divisor.component.html',
  styleUrls: ['./divisor.component.css']
})
export class DivisorComponent {

    @Input() titulo!: string;
    
    ngOnInit() {
      console.log('Divisor recebido:', this.titulo);
    }

}

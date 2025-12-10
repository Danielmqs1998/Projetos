import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {


  podeExibir: Boolean = false;

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      this.podeExibir = this.router.url === '/home';
    });
  }

}

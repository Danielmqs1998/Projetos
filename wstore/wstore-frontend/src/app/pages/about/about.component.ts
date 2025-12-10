import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent {

  num: string | null = "";

  @Input("teste")
  teste: string = "";

  texto: string = "";

  blocoExibido: boolean = false;

  constructor(private route: ActivatedRoute){
   
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.num = params.get("id");
    })
    this.route.queryParams.subscribe(params => {
      if(params["teste"]){
          this.teste = params['teste'];
      }
    })
  }

 
}

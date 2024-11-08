import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-psp-form',
  templateUrl: './psp-form.component.html',
  styleUrls: ['./psp-form.component.css']
})
export class PspFormComponent implements OnInit{

  constructor(private route:ActivatedRoute){}
  id: string | null = null;

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
    this.id = params.get('id'); 
   })
  }
  displayedColumns: string[] = ['name'];  // Definiši kolone
  cards = [
    { name: 'Cart'},
    { name: 'QR Code'},
    { name: 'PayPal'},
    { name: 'Bitcoin'}
  ];
  selectCard(card: any) {
  }

}

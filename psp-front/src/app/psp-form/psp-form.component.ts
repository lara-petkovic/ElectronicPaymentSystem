import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-psp-form',
  templateUrl: './psp-form.component.html',
  styleUrls: ['./psp-form.component.css']
})
export class PspFormComponent implements OnInit {
  @Output() paymentMethodSelected = new EventEmitter<{ name: string; id: string | null }>();
  
  id: string | null = null;
  displayedColumns: string[] = ['name'];
  cards = [
    { name: 'Cart' },
    { name: 'QR Code' },
    { name: 'PayPal' },
    { name: 'Bitcoin' }
  ];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id');
    });
  }

  selectCard(card: any) {
    // Emituj događaj sa izabranim načinom plaćanja i trenutnim ID-jem
    this.paymentMethodSelected.emit({ name: card.name, id: this.id });
  }
}

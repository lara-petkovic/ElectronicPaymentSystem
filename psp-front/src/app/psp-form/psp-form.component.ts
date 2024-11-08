import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-psp-form',
  templateUrl: './psp-form.component.html',
  styleUrls: ['./psp-form.component.css']
})
export class PspFormComponent implements OnInit{
  @Output() paymentMethodSelected = new EventEmitter<{ name: string; id: string | null }>();

  selectedRow: any = null;
  constructor(private route: ActivatedRoute) {}
  id: string | null = null;
  header: string="Choose one payment method"

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
    this.id = params.get('id'); 
   })
  }
  cards = [
    { name: 'Cart', id: '1' },
    { name: 'QR Code', id: '2' },
    { name: 'PayPal', id: '3' },
    { name: 'Bitcoin', id: '4' }
  ];

  displayedColumns: string[] = ['name'];

  selectCard(card: any) {
      if (this.selectedRow === card) {
        return; 
      }
    this.selectedRow = card;
    this.header="Choosen payment method - "+ card.name;
    this.paymentMethodSelected.emit({ name: card.name, id: this.id}); 
  }

  getFilteredCards() {
    return this.selectedRow ? [this.selectedRow] : this.cards;
  }
}

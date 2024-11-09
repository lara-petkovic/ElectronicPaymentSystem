import { Component, EventEmitter, Input, Output } from '@angular/core';

interface Card {
  name: string;
  id: string;
}

@Component({
  selector: 'app-psp-form',
  templateUrl: './psp-form.component.html',
  styleUrls: ['./psp-form.component.css']
})
export class PspFormComponent {
  @Output() paymentMethodSelected = new EventEmitter<{ name: string; id: string | null }>();

  selectedRow: any = null;
  header: string = "Choose one payment method";
  cards: Card[] = [];

  private _paymentOptions: string = '';

  // Setter za @Input paymentOptions koji ažurira listu kartica
  @Input()
  set paymentOptions(value: string) {
    this._paymentOptions = value;
    this.updateCards(); // Ažuriraj listu kartica kada se promeni paymentOptions
  }

  get paymentOptions(): string {
    return this._paymentOptions;
  }

  displayedColumns: string[] = ['name'];

  selectCard(card: any) {
    if (this.selectedRow === card) {
      return;
    }
    this.selectedRow = card;
    this.header = "Chosen payment method - " + card.name;
    this.paymentMethodSelected.emit({ name: card.name, id: card.id });
  }

  private updateCards() {
    this.cards = []; // Očisti postojeće kartice
    if (this.paymentOptions.includes("Card")) this.cards.push({ name: 'Card', id: '1' });
    if (this.paymentOptions.includes("QR Code")) this.cards.push({ name: 'QR Code', id: '2' });
    if (this.paymentOptions.includes("PayPal")) this.cards.push({ name: 'PayPal', id: '3' });
    if (this.paymentOptions.includes("Bitcoin")) this.cards.push({ name: 'Bitcoin', id: '4' });
  }

  getFilteredCards() {
    return this.selectedRow ? [this.selectedRow] : this.cards;
  }
}

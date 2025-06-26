import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { TransactionDto } from '../transactiondto.model';
import { PaymentOptionsServiceService } from '../payment-options-service.service';

interface Card {
  name: string;
  id: string;
}

@Component({
  selector: 'app-psp-form',
  templateUrl: './psp-form.component.html',
  styleUrls: ['./psp-form.component.css']
})
export class PspFormComponent implements OnInit{
  @Output() paymentMethodSelected = new EventEmitter<{ name: string; orderid: string | null; merchantid: string | null }>();

  constructor(private paymentOptionsService: PaymentOptionsServiceService){}
  selectedRow: any = null;
  header: string = "Choose one payment method";
  cards: Card[] = [];

  ngOnInit(): void {
    this.selectedRow = null;
  }

  private _paymentOptions: string = '';

  @Input()
  set paymentOptions(value: string) {
    this._paymentOptions = value;
    this.updateCards(); 
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
    alert('chosen payment method : ' + card.name);
  
    const jsonString = this._paymentOptions
      .replace(/'/g, '"')
      .replace(/(\w+)=/g, '"$1":');
    const data = JSON.parse(jsonString);

    // if (card.name === 'PayPal') {
    //   this.paymentOptionsService
    //     .getTransactionByMerchantOrderIdAndOrderId(data.merchantId, data.orderId)
    //     .subscribe(
    //       (transaction: TransactionDto) => {
    //         const transactionDetails = {
    //           orderId: transaction.orderId,
    //           merchantId: transaction.merchantId,
    //           amount: transaction.amount.toString(),
    //           timestamp: transaction.timestamp
    //         };

    //         const queryParams = new URLSearchParams(transactionDetails).toString();
    //         window.location.href = `https://localhost:4203?${queryParams}`;
    //       },
    //       (error: any) => {
    //         console.error('Error fetching transaction details:', error);
    //         alert('Failed to fetch transaction details.');
    //       }
    //     );
    // } else {
      this.paymentMethodSelected.emit({ name: card.name, orderid: data.orderId, merchantid: data.merchantId });
    //}
  
    this.selectedRow = null;
  }
  

  private updateCards() {
    this.cards = []; 
    if (this.paymentOptions.includes("Card")) this.cards.push({ name: 'Card', id: '1' });
    if (this.paymentOptions.includes("QR")) this.cards.push({ name: 'QR Code', id: '2' });
    if (this.paymentOptions.includes("PayPal")) this.cards.push({ name: 'PayPal', id: '3' });
    if (this.paymentOptions.includes("Crypto")) this.cards.push({ name: 'Crypto', id: '4' });
    // this.paymentOptions.split(',').forEach((option, index) => {
    //   const trimmedOption = option.trim(); 
    //   if (trimmedOption) {
    //     this.cards.push({ name: trimmedOption, id: (index + 1).toString() });
    //   }
    // });
  }

  getFilteredCards() {
    return this.selectedRow ? [this.selectedRow] : this.cards;
  }
}

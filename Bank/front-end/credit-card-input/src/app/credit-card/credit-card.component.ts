import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../payment.service';
import { WebSocketService } from '../websocket.service';

@Component({
  selector: 'app-credit-card',
  templateUrl: './credit-card.component.html',
  styleUrls: ['./credit-card.component.css']
})
export class CreditCardComponent {
  paymentId: number | undefined;
  constructor(private webSocketService: WebSocketService, private route: ActivatedRoute, private paymentService: PaymentService, private router: Router) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('paymentId');
      this.price = params.get('amount')!;
      if (id) {
        this.paymentId = Number(id);
      }
    });
  }
  price = '';
  cardDetails = {
    number: '',
    holder: '',
    expiry: '',
    ccv: ''
  };
  cardDetailsDto = {
    Pan: '',
    HolderName: '',
    ExpirationDate: '',
    SecurityCode: '',
    PaymentRequestId: 0
  }

  formatExpiryDate(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/[^\d]/g, '');

    if (value.length > 4) {
      value = value.slice(0, 4);
    }

    if (value.length >= 3) {
      value = value.slice(0, 2) + '/' + value.slice(2);
    }

    input.value = value;
  }
  formatCardNumber(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, '');

    let formattedValue = '';
    for (let i = 0; i < value.length; i += 4) {
      if (i + 4 <= value.length) {
        formattedValue += value.slice(i, i + 4) + ' ';
      } else {
        formattedValue += value.slice(i);
      }
    }
    formattedValue = formattedValue.trim();

    input.value = formattedValue;
  }

  handleBackspace(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    if (event.key === 'Backspace' && input.selectionEnd === input.value.length) {
      const lastChar = input.value.charAt(input.value.length - 1);
      if (lastChar === ' ') {
        input.value = input.value.slice(0, -1); 
      }
    }
  }

  submit(form:NgForm): void {
    this.cardDetailsDto.Pan = this.cardDetails.number.replace(/\s+/g, '');
    this.cardDetailsDto.SecurityCode = this.cardDetails.ccv;
    this.cardDetailsDto.HolderName = this.cardDetails.holder;
    this.cardDetailsDto.ExpirationDate = this.cardDetails.expiry;
    if(this.paymentId)
      this.cardDetailsDto.PaymentRequestId = this.paymentId ;
    if (form.valid) {
      //alert(this.cardDetailsDto.Pan);
      //this.paymentService.payWithCreditCard(this.cardDetailsDto);
      this.webSocketService.sendMessage(JSON.stringify(this.cardDetailsDto))
      this.router.navigate(['/']);
    } else {
      form.form.markAllAsTouched();
    }
  }
  isValidCardNumber(cardNumber: string): boolean {
    cardNumber = cardNumber.replace(/\D/g, '');
    let sum = 0;
    let shouldDouble = false;  
    for (let i = cardNumber.length - 1; i >= 0; i--) {
      let digit = parseInt(cardNumber.charAt(i));  
      if (shouldDouble) {
        digit *= 2;
        if (digit > 9) {
          digit -= 9;
        }
      }
      sum += digit;
      shouldDouble = !shouldDouble;
    }
    return sum % 10 === 0;
  }
  isValidExpirationDate(date: string): boolean{
    return date.startsWith("12") || date.startsWith("11") || date.startsWith("10") || date.startsWith("09") || date.startsWith("08") || date.startsWith("07") || date.startsWith("06") || date.startsWith("05") || date.startsWith("04") || date.startsWith("03") || date.startsWith("02") || date.startsWith("01")
  }
  
}

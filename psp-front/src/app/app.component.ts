import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // Ovdje ispravite putanju ako je potrebno
})
export class AppComponent {
  title = 'psp-front';
  id: string = '';
  clientId: string = '';
  showClientReg = false;
  showPaymentForm = false;
  private webSocket: WebSocket;
  private webSocketClient: WebSocket;
  paymentOptions: string = '';
  optionsP: string ='';

  constructor(private router: Router) {
    this.webSocket = new WebSocket('ws://localhost:8085/transactions');
    this.webSocketClient = new WebSocket('ws://localhost:8085/clients');

    this.webSocket.onmessage = (event) => {
      console.log(event.data);
      this.showPaymentForm = true;
      this.showClientReg = false;
      this.id = event.data;
      this.paymentOptions = event.data;
      this.router.navigate(['/payment-options', event.data]);
    };

    this.webSocketClient.onmessage = (event) => {
      console.log(event.data);
      this.showPaymentForm = false;
      this.showClientReg = true;
            const messageParts = event.data.split(',');
            this.clientId = messageParts[0].trim(); 
            const opcije = messageParts.slice(1).map((option: string) => option.trim());
            this.optionsP = opcije.join(','); 
            console.log('Parsed options:', this.optionsP);

      this.router.navigate(['/app-psp-client-reg']);
    };
  }

  sendPaymentMethod(selectedOption: { name: string; orderid: string | null; merchantid: string | null }) {
    const message = JSON.stringify(selectedOption);
    this.webSocket.send(message);
    console.log('Sent option:', message);
  }

  handleOptionSelected(option: { name: string; clientId: string | null }) {
    option.clientId = this.clientId;
    const message = JSON.stringify(option);
    this.webSocketClient.send(message);
    console.log('Sent options:', message);
  }
}

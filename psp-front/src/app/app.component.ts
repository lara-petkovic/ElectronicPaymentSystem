import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'psp-front';
  id:string='';
  clientId:string='';
  showClientReg = false;
  showPaymentForm = false;
  private webSocket: WebSocket;
  private webSocketClient: WebSocket;
  paymentOptions: string = ''; // Dodaj paymentOptions string

  constructor(private router: Router) {
    this.webSocket = new WebSocket('ws://localhost:8085/transactions');
    this.webSocketClient = new WebSocket('ws://localhost:8085/clients');

    this.webSocket.onmessage = (event) => {
      console.log(event.data);
      this.showPaymentForm = true;
      this.showClientReg = false;
      this.id=event.data
      this.paymentOptions = event.data; // Ažuriraj paymentOptions sa event.data
      this.router.navigate(['/payment-options', event.data]);
    };

    this.webSocketClient.onmessage = (event) => {
      console.log(event.data);
      this.showPaymentForm = false;
      this.showClientReg = true;
      this.clientId=event.data
      this.router.navigate(['/app-psp-client-reg']);
    };
  }

  sendPaymentMethod(selectedOption: { name: string; id: string | null }) {
    console.log("hhhhhhhhhhhhh")
    selectedOption.id=this.id
    const message = JSON.stringify(selectedOption);
    this.webSocket.send(message);
    console.log('Sent option:', message);
  }
  handleOptionSelected(option: { name: string; clientId : string | null}) {
    option.clientId=this.clientId
    const message = JSON.stringify(option);
    console.log("usao je ovde"+option.name)
    this.webSocketClient.send(message);
    console.log('Sent options:', message);
  }
}

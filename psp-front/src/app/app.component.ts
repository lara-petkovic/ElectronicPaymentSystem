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
  private webSocket!: WebSocket;
  private webSocketClient!: WebSocket;
  private webSocketResponse!: WebSocket;
  paymentOptions: string = '';
  optionsP: string = '';

  constructor(private router: Router) {
    this.initializeWebSockets();
/*
    setInterval(() => {
      this.sendPing();
    }, 1500);
    */
  }

  private initializeWebSockets() {
    this.setupWebSocket('transactions');
    this.setupWebSocket('clients');
    this.setupWebSocket('responses');
  }

  private setupWebSocket(endpoint: string) {
    const url = `ws://localhost:8085/${endpoint}`;
    const webSocket = new WebSocket(url);

    webSocket.onopen = () => {
      console.log(`WebSocket connection to ${endpoint} established.`);
    };

    webSocket.onclose = (event) => {
      console.log(`WebSocket connection to ${endpoint} closed. Reconnecting...`);
      setTimeout(() => this.setupWebSocket(endpoint), 1000); // Ponovni pokušaj nakon 5 sekundi
    };

    webSocket.onerror = (error) => {
      console.error(`WebSocket error on ${endpoint}:`, error);
      webSocket.close(); // Zatvori konekciju da bi se pokrenuo onclose handler
    };

    webSocket.onmessage = (event) => {
      console.log(`Message from ${endpoint}:`, event.data);
      // Obradi poruku na osnovu endpoint-a
      if (endpoint === 'transactions') {
        this.handleTransactionMessage(event);
      } else if (endpoint === 'clients') {
        this.handleClientMessage(event);
      } else if (endpoint === 'responses') {
        this.handleResponseMessage(event);
      }
    };

    // Sačuvaj referencu na WebSocket
    if (endpoint === 'transactions') {
      this.webSocket = webSocket;
    } else if (endpoint === 'clients') {
      this.webSocketClient = webSocket;
    } else if (endpoint === 'responses') {
      this.webSocketResponse = webSocket;
    }
  }
/*
  private sendPing() {
    if (this.webSocket?.readyState === WebSocket.OPEN) {
      this.webSocket.send('ping');
    }
    if (this.webSocketClient?.readyState === WebSocket.OPEN) {
      this.webSocketClient.send('ping');
    }
    if (this.webSocketResponse?.readyState === WebSocket.OPEN) {
      this.webSocketResponse.send('ping');
    }
  }
  */

  private handleTransactionMessage(event: MessageEvent) {
    console.log(event.data);
    this.showPaymentForm = true;
    this.showClientReg = false;
    this.id = event.data;
    this.paymentOptions = event.data;
    this.router.navigate(['/payment-options', event.data]);
  }

  private handleClientMessage(event: MessageEvent) {
    console.log(event.data);
    this.showPaymentForm = false;
    this.showClientReg = true;
    const messageParts = event.data.split(',');
    this.clientId = messageParts[0].trim();
    const opcije = messageParts.slice(1).map((option: string) => option.trim());
    this.optionsP = opcije.join(',');
    console.log('Parsed options:', this.optionsP);
    this.router.navigate(['/app-psp-client-reg']);
  }

  private handleResponseMessage(event: MessageEvent) {
    console.log(event.data);
    this.showPaymentForm = false;
    this.showClientReg = false;
    if (event.data.includes('success')) {
      this.router.navigate(['success']);
    }
    if (event.data.includes('error')) {
      this.router.navigate(['error']);
    }
    if (event.data.includes('fail')) {
      this.router.navigate(['fail']);
    }
  }

  sendPaymentMethod(selectedOption: { name: string; orderid: string | null; merchantid: string | null }) {
    const message = JSON.stringify(selectedOption);
    this.webSocket.send(message);
    console.log('Sent option:', message);
    this.showClientReg = false;
    this.showPaymentForm = false;
    this.router.navigate(['home']);
  }

  handleOptionSelected(option: { name: string; clientId: string | null }) {
    option.clientId = this.clientId;
    const message = JSON.stringify(option);
    this.webSocketClient.send(message);
    console.log('Sent options:', message);
    this.showClientReg = false;
    this.showPaymentForm = false;
    this.router.navigate(['home']);
  }
}

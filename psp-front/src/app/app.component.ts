import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'psp-front';
  id:string=''

  private webSocket: WebSocket;

  constructor(private router: Router) {
    this.webSocket = new WebSocket('ws://localhost:8085/transactions');
    this.webSocket.onmessage = (event) => {
      console.log(event.data);
      this.id=event.data
      this.router.navigate(['/payment-options', event.data]);
    };
  }

  // Metod za slanje podataka serveru putem WebSocket-a
  sendPaymentMethod(selectedOption: { name: string; id: string | null }) {
    selectedOption.id=this.id
    const message = JSON.stringify(selectedOption);
    this.webSocket.send(message);
    console.log('Sent:', message);
  }
}

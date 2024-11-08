import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'psp-front';
  stock: any = {};

  private webSocket: WebSocket;

  constructor() {
    this.webSocket = new WebSocket('ws://localhost:8085/transactions');
    this.webSocket.onmessage = (event) => {
      this.stock = event.data
      console.log(this.stock)
    };
  } 
}

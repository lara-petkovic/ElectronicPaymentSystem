import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'psp-front';

  private webSocket: WebSocket;

  constructor(router:Router) {
    this.webSocket = new WebSocket('ws://localhost:8085/transactions');
    this.webSocket.onmessage = (event) => {
      console.log(event.data)
      router.navigate(['/payment-options', event.data]);

    };
  } 
}

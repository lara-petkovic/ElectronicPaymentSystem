import { Component, OnInit, OnDestroy } from '@angular/core';
import { WebSocketService } from './websocket.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'credit-card-input';
  message: string = '';
  messages: string[] = [];

  constructor(private webSocketService: WebSocketService, private router: Router) {}

  ngOnInit() {
    this.webSocketService.connect('ws://localhost:8052/creditCards');

    this.webSocketService.getMessages().subscribe((message: string) => {
    console.log('Message received from WebSocket:', message);
    this.processMessage(message);
  });
  }

  processMessage(message: string) {
    if (message.split(',').length === 2) {
      this.router.navigate(['credit-card-input/' + message.split(',')[0] + '/' + message.split(',')[1]]);
    }
  }

  ngOnDestroy() {
  }
}

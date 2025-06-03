import { Component, OnInit, OnDestroy } from '@angular/core';
import { WebSocketService } from './websocket.service';
import { Router } from '@angular/router';
import { ImageService } from './image.service';
import { PaymentService } from './payment.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'credit-card-input';
  message: string = '';
  messages: string[] = [];

  constructor(private webSocketService: WebSocketService, private router: Router, private imageService: ImageService) {}

  ngOnInit() {
    this.webSocketService.connect('wss://localhost:8052/creditCards');

    this.webSocketService.getMessages().subscribe((message: string) => {
    console.log('Message received from WebSocket:', message);
    this.processMessage(message);
  });
  }

  processMessage(message: any) {
    if (message.length===0){
      this.router.navigate([''])
    }
    else if (message.startsWith('qr:')) {
      let src = `data:image/png;base64,${message.substring(3)}`;
      this.imageService.setImage(src);
      this.router.navigate(['qr-code']);
    }
    else if (message.split(',').length === 2) {
      this.router.navigate(['credit-card-input/' + message.split(',')[0] + '/' + message.split(',')[1]]);
    }
  }
  ngOnDestroy() {
  }
}

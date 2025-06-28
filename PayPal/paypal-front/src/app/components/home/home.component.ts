import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { PaymentRequestDto } from '../../models/PaymentRequestDto';
import { TransactionStateService } from '../../transaction-state.service';
import { CheckoutComponent } from '../checkout/checkout.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CheckoutComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  private stompClient!: Client;
  private subscription!: StompSubscription;
  notifications: string[] = [];

  constructor(
    private router: Router,
    private transactionStateService: TransactionStateService
  ) { }

  ngOnInit(): void {
    this.connect();
  }

  ngOnDestroy(): void {
    this.disconnect();
  }

  connect(): void {
    this.stompClient = new Client({
      brokerURL: 'wss://localhost:8089/ws',
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log('Connected');
        this.subscription = this.stompClient.subscribe('/topic/notification', (message: IMessage) => {
          const dto = JSON.parse(message.body);
          this.showNotification(dto);
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      }
    });

    this.stompClient.activate();
  }

  disconnect(): void {
    if (this.stompClient && this.stompClient.active) {
      this.subscription?.unsubscribe();
      this.stompClient.deactivate();
      console.log('Disconnected');
    }
  }

  private showNotification(dto: PaymentRequestDto): void {
    console.log('Received notification DTO:', dto);
    this.transactionStateService.setTransaction(dto);
    this.router.navigate(['/checkout']);
  }
}

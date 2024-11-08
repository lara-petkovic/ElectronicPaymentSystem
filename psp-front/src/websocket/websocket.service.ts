import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';


@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  constructor() { }

  connect() {
    const stompClient = new Client({
      brokerURL: 'ws://localhost:8085/ws',
      reconnectDelay: 5000, 
      debug: (str) => {
        console.log(str);
      }
    });
    stompClient.onConnect = (frame) => {
      console.log('Connected:', frame);
      const subscription = stompClient.subscribe('/topic/messages', (message: Message) => {
        const receivedData = JSON.parse(message.body);
        console.log('Received message:', receivedData);
      });
    };

    stompClient.onStompError = (frame) => {
      console.error('Broker reported error:', frame.headers['message']);
      console.error('Additional details:', frame.body);
    };

    stompClient.activate();
  }
}

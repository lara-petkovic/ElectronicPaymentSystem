import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'  // Singleton service
})
export class WebSocketService {
  private socket!: WebSocket;
  private messageSubject: Subject<string> = new Subject<string>();

  constructor() {}

  connect(url: string): Promise<void> {
    console.log('Attempting to connect to WebSocket at', url);

    return new Promise((resolve, reject) => {
      this.socket = new WebSocket(url);

      this.socket.onopen = () => {
        console.log('WebSocket connection established');
        resolve();  // Resolve the promise when connection is established
      };

      this.socket.onmessage = (event) => {
        console.log('Received message from WebSocket:', event.data);
        this.messageSubject.next(event.data);  // Emit received messages
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        reject(error);  // Reject the promise in case of an error
      };

      this.socket.onclose = () => {
        console.log('WebSocket connection closed');
      };
    });
  }

  sendMessage(message: string): void {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
    } else {
      console.warn('WebSocket is not open. Unable to send message.');
    }
  }

  getMessages() {
    return this.messageSubject.asObservable();
  }

  disconnect(): void {
    if (this.socket) {
      console.log('Closing WebSocket connection...');
      this.socket.close();
    }
  }
}

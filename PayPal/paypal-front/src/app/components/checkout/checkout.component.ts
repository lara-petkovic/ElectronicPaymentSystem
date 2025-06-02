import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { environment } from '../../../enviroment';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  transactionDetails: any;
  isBrowser: boolean;

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit() {
    if (this.isBrowser) {
      const queryParams = new URLSearchParams(window.location.search);
      const orderId = queryParams.get('orderId');
      const merchantId = queryParams.get('merchantId');
      const amount = queryParams.get('amount');
      const timestamp = queryParams.get('timestamp');

      if (orderId && merchantId && amount && timestamp) {
        this.transactionDetails = { orderId, merchantId, amount, timestamp };
        this.loadPayPalButton();
      } else {
        console.error('Transaction details not found in query parameters!');
      }
    }
  }

  loadPayPalButton() {
    if (!this.isBrowser) return;
  
    const paypal = (window as any).paypal;
  
    if (paypal && this.transactionDetails) {
      paypal.Buttons({
        createOrder: async (data: any, actions: any) => {
          try {
            const response = await fetch(`${environment.apiBaseUrl}/api/payments/create`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify({
                orderId: this.transactionDetails.orderId,
                merchantId: this.transactionDetails.merchantId,
                amount: this.transactionDetails.amount,
              }),
            });
  
            if (!response.ok) {
              throw new Error('Failed to create order');
            }
  
            const result = await response.json();
            return result.paypalOrderId;
          } catch (error) {
            console.error('Error creating PayPal order:', error);
            throw error;
          }
        },
        onApprove: async (data: any, actions: any) => {
          try {
            const response = await this.http.post(
              `${environment.apiBaseUrl}/api/payments/capture`,
              { orderId: data.orderID }
            ).toPromise();
  
            console.log('Payment successful:', response);
  
            const queryParams = new URLSearchParams({
              orderId: this.transactionDetails.orderId,
              merchantId: this.transactionDetails.merchantId,
              amount: this.transactionDetails.amount,
              timestamp: this.transactionDetails.timestamp,
            }).toString();
  
            window.location.href = `/success?${queryParams}`;
          } catch (error) {
            console.error('Error capturing payment:', error);
            alert('Payment capture failed. Please try again.');
          }
        },
        onError: (err: any) => {
          console.error('Error during PayPal payment:', err);
          alert('An error occurred. Please try again.');
        },
      }).render('#paypal-button-container');
    } else {
      console.error('PayPal SDK not loaded or transaction details missing!');
    }
  }
}  
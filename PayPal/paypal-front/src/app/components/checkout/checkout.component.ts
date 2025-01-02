import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  transactionDetails: any;

  ngOnInit() {
    const queryParams = new URLSearchParams(window.location.search);
    const orderId = queryParams.get('orderId');
    const merchantId = queryParams.get('merchantId');
    const amount = queryParams.get('amount');
    const timestamp = queryParams.get('timestamp');

    if (orderId && merchantId && amount && timestamp) {
      this.transactionDetails = {
        orderId,
        merchantId,
        amount,
        timestamp
      };
      this.loadPayPalButton();
    } else {
      console.error('Transaction details not found in query parameters!');
    }
  }

  loadPayPalButton() {
    const paypal = (window as any).paypal;

    if (paypal && this.transactionDetails) {
      paypal.Buttons({
        createOrder: (data: any, actions: any) => {
          return actions.order.create({
            purchase_units: [
              {
                description: `Order ID: ${this.transactionDetails.orderId}`,
                amount: {
                  currency_code: 'USD',
                  value: this.transactionDetails.amount
                }
              }
            ]
          });
        },
        onApprove: async (data: any, actions: any) => {
          const order = await actions.order.capture();
          console.log('Order successfully paid:', order);

          // Redirect to success page with transaction details in query parameters
          const queryParams = new URLSearchParams({
            orderId: this.transactionDetails.orderId,
            merchantId: this.transactionDetails.merchantId,
            amount: this.transactionDetails.amount,
            timestamp: this.transactionDetails.timestamp
          }).toString();

          window.location.href = `/success?${queryParams}`;
        },
        onError: (err: any) => {
          console.error('Error during PayPal payment:', err);
          alert('An error has occurred. Please try again.');
        }
      }).render('#paypal-button-container');
    } else {
      console.error('PayPal SDK not loaded or transaction details missing!');
    }
  }
}
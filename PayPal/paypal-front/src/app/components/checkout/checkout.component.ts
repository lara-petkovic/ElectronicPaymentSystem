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
  selectedProduct: any;

  ngOnInit() {
    const product = localStorage.getItem('selectedProduct');
    this.selectedProduct = product ? JSON.parse(product) : null;

    if (this.selectedProduct) {
      this.loadPayPalButton();
    }
  }

  loadPayPalButton() {
    const paypal = (window as any).paypal;

    if (paypal) {
      paypal.Buttons({
        createOrder: (data: any, actions: any) => {
          return actions.order.create({
            purchase_units: [{
              description: this.selectedProduct.name,
              amount: {
                value: this.selectedProduct.price
              }
            }]
          });
        },
        onApprove: async (data: any, actions: any) => {
          const order = await actions.order.capture();
          console.log('Order successfully paid:', order);
          alert('Uspešno plaćanje!');
          localStorage.removeItem('selectedProduct');
        },
        onError: (err: any) => {
          console.error('Error during PayPal payment:', err);
          alert('Došlo je do greške tokom plaćanja.');
        }
      }).render('#paypal-button-container');
    } else {
      console.error('PayPal SDK nije učitan!');
    }
  }
}
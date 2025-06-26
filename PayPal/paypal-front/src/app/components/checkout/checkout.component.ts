import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, Inject, Input, OnInit, PLATFORM_ID } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../../enviroment';
import { PaymentRequestDto } from '../../models/PaymentRequestDto';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  @Input() transactionDetails: PaymentRequestDto | null = null;
  isBrowser: boolean;

  error: string | null = null;
  loading: boolean = true;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object,
    private route: ActivatedRoute
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['orderId'] && params['amount']) {
        this.transactionDetails = {
          orderId: params['orderId'],
          merchantId: params['merchantId'] ?? '',
          amount: +params['amount']
        };
        this.loading = false;
        this.loadPayPalButton();
      } else {
        this.handleError('Missing payment parameters');
      }
    });
  }

  handleError(message: string) {
    this.error = message;
    this.loading = false;
    console.error(message);
  }

  loadPayPalButton() {
    if (!this.isBrowser || !this.transactionDetails) return;

    const paypal = (window as any).paypal;

    if (!paypal) {
      this.handleError('PayPal SDK not loaded!');
      return;
    }

    setTimeout(() => {
      const container = document.getElementById('paypal-button-container');
      if (!container) {
        this.handleError('PayPal button container not found in DOM.');
        return;
      }

      paypal.Buttons({
        createOrder: async () => {
          try {
            const response = await fetch(`${environment.apiBaseUrl}/api/payments/create`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(this.transactionDetails),
            });

            if (!response.ok) throw new Error('Failed to create order');
            const result = await response.json();
            return result.paypalOrderId;
          } catch (error) {
            this.handleError('Error creating PayPal order.');
            throw error;
          }
        },
        onApprove: async (data: any) => {
          try {
            // await this.http.post(`${environment.apiBaseUrl}/api/payments/capture`, {
            //   orderId: data.orderID
            // }).toPromise();

            const queryParams = new URLSearchParams({
              orderId: this.transactionDetails!.orderId,
              merchantId: this.transactionDetails!.merchantId ?? '',
              amount: this.transactionDetails!.amount.toString(),
              timeStamp: new Date().toISOString()
            }).toString();

            window.location.href = `/success?${queryParams}`;
          } catch (error) {
            this.handleError('Payment capture failed. Please try again.');
          }
        },
        onError: (err: any) => {
          this.handleError('An error occurred. Please try again.');
          console.error('PayPal error:', err);
        }
      }).render('#paypal-button-container');
    }, 0);
  }
}

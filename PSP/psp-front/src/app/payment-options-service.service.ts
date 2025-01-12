import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentOptionDto } from './options.model';
import { TransactionDto } from './transactiondto.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentOptionsServiceService {

  private paymentOptionApiUrl = 'https://localhost:8085/api/paymentoption';
  private responseApiUrl = 'https://localhost:8085/api/response';

  constructor(private http: HttpClient) { }

  getAllOptions(): Observable<PaymentOptionDto[]> {
    return this.http.get<PaymentOptionDto[]>(this.paymentOptionApiUrl);
  }

  addPaymentOption(newOption: PaymentOptionDto): Observable<PaymentOptionDto> {
    return this.http.post<PaymentOptionDto>(this.paymentOptionApiUrl, newOption);
  }

  removePaymentOption(option: PaymentOptionDto): Observable<void> {
    return this.http.delete<void>(this.paymentOptionApiUrl, { body: option });
  }

  getTransactionByMerchantOrderIdAndOrderId(merchantOrderId: string, orderId: number): Observable<TransactionDto> {
    const url = `${this.responseApiUrl}/by-merchant-order-id/${merchantOrderId}/order-id/${orderId}`;
    return this.http.get<TransactionDto>(url);
  }
}

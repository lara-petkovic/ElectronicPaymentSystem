import { Injectable } from '@angular/core';
import { PaymentRequestDto } from './models/PaymentRequestDto';

@Injectable({
  providedIn: 'root'
})
export class TransactionStateService {
  private paymentRequest: PaymentRequestDto | null = null;

  setTransaction(dto: PaymentRequestDto): void {
    this.paymentRequest = dto;
  }

  getTransaction(): PaymentRequestDto | null {
    return this.paymentRequest;
  }

  clearTransaction(): void {
    this.paymentRequest = null;
  }
}
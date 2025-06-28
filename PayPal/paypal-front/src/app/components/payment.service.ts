import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../enviroment';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) { }

  capturePayment(orderId: string): Observable<any> {
    const params = new HttpParams().set('orderId', orderId);
    return this.http.post(`${environment.apiBaseUrl}/api/payments/capture`, null, { params });
  }
}

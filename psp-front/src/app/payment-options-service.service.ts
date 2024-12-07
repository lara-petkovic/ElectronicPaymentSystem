import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentOptionDto } from './options.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentOptionsServiceService {

  private apiUrl = 'http://localhost:8085/api/paymentoption';  // Putanja do tvoje API kontrolera

  constructor(private http: HttpClient) { }

  // Metoda za dobijanje svih opcija plačanja
  getAllOptions(): Observable<PaymentOptionDto[]> {
    return this.http.get<PaymentOptionDto[]>(this.apiUrl);  // HTTP GET poziv
  }

  // Metoda za dodavanje nove opcije plačanja
  addPaymentOption(newOption: PaymentOptionDto): Observable<PaymentOptionDto> {
    return this.http.post<PaymentOptionDto>(this.apiUrl, newOption);  // HTTP POST poziv
  }

  // Metoda za uklanjanje opcije plačanja
  removePaymentOption(option: PaymentOptionDto): Observable<void> {
    return this.http.delete<void>(this.apiUrl, { body: option });  // HTTP DELETE poziv
  }
}

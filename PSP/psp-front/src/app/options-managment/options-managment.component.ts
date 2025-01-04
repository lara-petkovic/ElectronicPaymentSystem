import { Component, OnInit } from '@angular/core';
import { PaymentOptionDto } from '../options.model';
import { PaymentOptionsServiceService } from '../payment-options-service.service';

@Component({
  selector: 'app-options-managment',
  templateUrl: './options-managment.component.html',
  styleUrls: ['./options-managment.component.css']
})
export class OptionsManagmentComponent implements OnInit{
  paymentOptions: PaymentOptionDto[] = [];  // Lista opcija plačanja
  newOptionName: string = '';  // Polje za unos nove opcije

  constructor(private paymentOptionsService: PaymentOptionsServiceService) {}

  ngOnInit(): void {
    // Inicijalizuj opcije plačanja pri učitavanju komponente
    this.loadPaymentOptions();
  }

  // Funkcija za učitavanje svih opcija plačanja
  loadPaymentOptions(): void {
    this.paymentOptionsService.getAllOptions().subscribe(options => {
      this.paymentOptions = options;
    });
  }

  // Funkcija za dodavanje nove opcije
  addPaymentOption(): void {
    if (this.newOptionName.trim()) {
      const newOption: PaymentOptionDto = { name: this.newOptionName.trim() };
      this.paymentOptionsService.addPaymentOption(newOption).subscribe((option) => {
        this.paymentOptions.push(option);  // Dodaj novu opciju u listu
        this.newOptionName = '';  // Resetuje polje za unos
      });
    }
  }

  // Funkcija za uklanjanje opcije
  removePaymentOption(index: number): void {
    if (this.paymentOptions.length > 1) {
      const optionToRemove = this.paymentOptions[index];
      this.paymentOptionsService.removePaymentOption(optionToRemove).subscribe(() => {
        this.paymentOptions.splice(index, 1);  // Ukloni opciju iz liste
      });
    } else {
      alert('Last payment option cannot be removed!');
    }
  }

}

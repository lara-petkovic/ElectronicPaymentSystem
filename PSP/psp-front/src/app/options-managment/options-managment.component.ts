import { Component, OnInit } from '@angular/core';
import { PaymentOptionDto } from '../options.model';
import { PaymentOptionsServiceService } from '../payment-options-service.service';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-options-managment',
  templateUrl: './options-managment.component.html',
  styleUrls: ['./options-managment.component.css']
})
export class OptionsManagmentComponent implements OnInit{
  paymentOptions: PaymentOptionDto[] = [];  
  newOptionName: string = '';  

  constructor(private paymentOptionsService: PaymentOptionsServiceService, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.loadPaymentOptions();
  }

  loadPaymentOptions(): void {
    this.paymentOptionsService.getAllOptions().subscribe(options => {
      this.paymentOptions = options;
    });
  }

  addPaymentOption(): void {
    if (this.newOptionName.trim()) {
      const newOption: PaymentOptionDto = { name: this.newOptionName.trim() };
      this.paymentOptionsService.addPaymentOption(newOption).subscribe((option) => {
        this.paymentOptions.push(option);  
        this.newOptionName = ''; 
      });
      this.loadPaymentOptions();
    }
  }

  removePaymentOption(index: number): void {
    if (this.paymentOptions.length > 1) {
      const optionToRemove = this.paymentOptions[index];
      this.paymentOptionsService.removePaymentOption(optionToRemove).subscribe(() => {
        this.paymentOptions.splice(index, 1);  
      });
    } else {
      alert('Last payment option cannot be removed!');
    }
  }
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
  
}

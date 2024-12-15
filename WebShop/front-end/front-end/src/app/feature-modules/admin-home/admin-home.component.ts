import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';

@Component({
  selector: 'xp-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {
  paymentOptions: any[] = [];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPaymentOptions();
  }

  onButtonClick(): void {
    this.authService.subscribe();
  }

  logout(): void {
    this.router.navigate(['/login']);
  }

  loadPaymentOptions(): void {
    this.authService.getPaymentOptions().subscribe({
      next: (options) => {
        this.paymentOptions = options;
      },
      error: (err) => {
        console.error('Failed to load payment options', err);
      }
    });
  }

  removePaymentOption(option: any): void {
    this.authService.removePaymentOption(option).subscribe({
      next: () => {
        console.log('Removed payment option:', option.name);
        this.loadPaymentOptions();
      },
      error: (err) => {
        console.error('Failed to remove payment option', err);
      }
    });
    this.loadPaymentOptions();
  }  
}
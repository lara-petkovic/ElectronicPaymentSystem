import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { Service } from 'src/app/infrastructure/auth/model/service.model';
import { FeatureModulesServicesService } from '../feature-modules-services.service';
import { Package } from 'src/app/infrastructure/auth/model/package.model';
import { Subscription } from 'src/app/infrastructure/auth/model/subscription.model';

@Component({
  selector: 'xp-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userId: number;
  services: Service[] = [];
  packages: Package[] = [];
  serviceTypeMapping: { [key: number]: string } = {
    0: 'Mobile Phone',
    1: 'Fixed Phone',
    2: 'Internet',
    3: 'Digital TV'
  };

  constructor(
    private authService: AuthService,
    private router: Router,
    private service: FeatureModulesServicesService
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.user$.value.id;

    this.service.getServices().subscribe({
      next: (services) => {
        this.services = services;
      },
      error: (err) => {
        console.error('Error fetching services:', err);
      }
    });

    this.service.getPackages().subscribe({
      next: (packages) => {
        this.packages = packages;
      },
      error: (err) => {
        console.error('Error fetching packages:', err);
      }
    });
  }

  onSubscribeToPackage(packageId: number, durationInYears: number): void {
    const subscription: Subscription = {
      id: 0,
      userId: this.userId, 
      packageId: packageId,
      startDate: new Date(), 
      durationInYears: durationInYears
    };
  
    this.service.createSubscription(subscription).subscribe({
      next: (createdSubscription) => {
        console.log('Subscription created:', createdSubscription);
        this.router.navigate(['/subscription-success']);
      },
      error: (err) => {
        console.error('Error creating subscription:', err);
        alert('Failed to create subscription. Please try again.');
      }
    });
  }

  logout() {
    this.authService.logout();
  }

  login() {
    this.router.navigate(['/login']);
  }

  getServiceTypeLabel(type: string): string {
    return this.serviceTypeMapping[Number(type)] || 'Unknown';
  }

  onSubscribe(): void {
    this.authService.subscribe();
  }
}

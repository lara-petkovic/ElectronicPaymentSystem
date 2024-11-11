import { Component, OnInit, Input, ViewChild, ElementRef} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { Service } from 'src/app/infrastructure/auth/model/service.model';
import { FeatureModulesServicesService } from '../feature-modules-services.service';
import { Package } from 'src/app/infrastructure/auth/model/package.model';

@Component({
  selector: 'xp-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})


export class HomeComponent implements OnInit{
  userId: number;
  services: Service[] = [];
  packages: Package[] = [];
  
  constructor(private authService: AuthService, private router: Router, private service: FeatureModulesServicesService) { }
  
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

  logout(){
    this.authService.logout();
  }

  login(){
    this.router.navigate(['/login']);
  }
}
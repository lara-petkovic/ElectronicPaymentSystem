import { Component } from '@angular/core';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';

@Component({
  selector: 'xp-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent {

  constructor(
    private authService: AuthService
  ) {}
  
  onButtonClick(): void {
    this.authService.subscribe();
  }
}

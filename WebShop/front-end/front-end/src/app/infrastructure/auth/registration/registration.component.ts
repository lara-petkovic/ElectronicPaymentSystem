import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
import { User } from '../model/user.model';

@Component({
  selector: 'xp-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  waitingForVerification: boolean = false;
  isVerified: boolean = false;

  registrationForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.pattern('^[A-Z][a-z]*$')]),
    surname: new FormControl('', [Validators.required, Validators.pattern('^[A-Z][a-z]*$')]),
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  });

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {
    if (this.registrationForm.valid) {
      const newUser: User = {
        id: 0,
        username: this.registrationForm.get('username')?.value || '',
        firstName: this.registrationForm.get('name')?.value || '',
        lastName: this.registrationForm.get('surname')?.value || '',
        email: this.registrationForm.get('email')?.value || '',
        password: this.registrationForm.get('password')?.value || '',
        subscriptions: null!,
        role: "USER"
      };
  
      this.authService.register(newUser).subscribe({
        next: () => {
          this.router.navigate(['login']);
        },
        error: (error) => {
          console.error('Registration error:', error);
          alert(`Registration failed: ${error.message || 'Unknown error'}`);
        }
      });
    } else {
      console.error('Form is invalid:', this.registrationForm.errors);
    }
  }  
}
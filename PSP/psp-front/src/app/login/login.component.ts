import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) { }

  sanitizeInput(input: string): string {
    const sanitized = this.sanitizer.sanitize(1, input); 
    return sanitized ?? ''; 
  }
  
  onLogin(): void {
    this.username = this.sanitizeInput(this.username);
    this.password = this.sanitizeInput(this.password);
  
    this.authService.authenticate(this.username, this.password).subscribe({
      next: (response) => {
        const token = response.headers.get('authorization');
        if (token) {
          this.authService.saveToken(token);
          this.router.navigate(['/manage']);
        } else {
          this.errorMessage = 'Login successful but token is missing.';
        }
      },
      error: (err) => {
        if (err.status === 423) { // HttpStatus.LOCKED
          this.errorMessage = 'Your account is locked. Please try again after 5 minutes.';
        } else if (err.status === 401) { // HttpStatus.UNAUTHORIZED
          this.errorMessage = 'Invalid username or password.';
        } else {
          this.errorMessage = 'An unexpected error occurred.';
        }
        alert(this.errorMessage);
      },
    });
  }
  
}

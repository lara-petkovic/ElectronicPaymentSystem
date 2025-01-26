import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  tfacode: string = ''; // Polje za unos 2FA koda
  errorMessage: string = '';
  qrCodeUrl: SafeUrl | null = null; // Sigurna URL za QR kod
  showQrCode: boolean = false;

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
    this.tfacode = this.sanitizeInput(this.tfacode);

    this.authService.authenticate(this.username, this.password, this.tfacode).subscribe({
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
          this.errorMessage = 'Invalid username, password, or 2FA code.';
        } else {
          this.errorMessage = 'An unexpected error occurred.';
        }
        alert(this.errorMessage);
      },
    });
  }

  generateQrCode(): void {
    this.authService.getQrCode(this.username).subscribe({
      next: (response: string) => {
        this.qrCodeUrl = this.sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${response}`);
        this.showQrCode = true;
      },
      error: (err) => {
        console.error('Error fetching QR code:', err);
        this.errorMessage = 'Failed to load QR code. Make sure the username is correct.';
      }
    });
  }
}

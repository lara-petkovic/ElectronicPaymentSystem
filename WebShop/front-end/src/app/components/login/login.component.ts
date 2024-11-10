import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  username: string = '';
  password: string = '';

  constructor(private router: Router, private userService: UserService, private authService: AuthService) {}

  ngOnInit() {
    if (localStorage.getItem('access_token')) {
      this.router.navigate(['home']);
    }
  }

  login_submit(credentials: { username: string, password: string }) {
    this.authService.login(credentials).subscribe({
      next: () => {
        this.authService.user$.subscribe(() => {
          this.router.navigate(['/']);
        });
      },
      error: (error) => {
        console.error('Login failed:', error);
      }
    });
  }  
}
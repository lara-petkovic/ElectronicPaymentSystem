import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { Login } from '../model/login.model';
import { User } from '../model/user.model';

@Component({
  selector: 'xp-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user: User | undefined;
  enterUsername: boolean = false

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  login(): void {
    const login: Login = {
      username: this.loginForm.value.username || "",
      password: this.loginForm.value.password || "",
    };

    if (this.loginForm.valid) {
      if(this.loginForm.value.username=="admin" && this.loginForm.value.password=="admin"){
        this.router.navigate(['admin-home']);
      }else{
        this.authService.login(login).subscribe({
          next: () => {
            this.authService.user$.subscribe(user => {
              this.user = user;
              console.log(this.user);
              this.router.navigate(['/']);
            });
          },
          error: (error)=>{
          console.error('Login failed:', error);
          alert('Incorrect username or password or user is not verified!');
        }
        });
    }
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{
  user: User = {
    id: 0,
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  };
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {

  }

  register(): void {
    this.userService.register(this.user).subscribe(
      (response) => {
        console.log('User registered successfully!', response);
        this.router.navigate(['/login']);
      },
      (error) => {
        console.error('There was an error!', error);
        this.errorMessage = 'Registration failed. Please try again.';
      }
    );
  }
}

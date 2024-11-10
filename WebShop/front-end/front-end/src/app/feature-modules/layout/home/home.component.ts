import { Component, OnInit, Input, ViewChild, ElementRef} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';

@Component({
  selector: 'xp-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})


export class HomeComponent implements OnInit{
  userId: number;
  
  constructor(private authService: AuthService, private router: Router) { }
  
  ngOnInit(): void {
    this.userId = this.authService.user$.value.id;
  }

  logout(){
    this.authService.logout();
  }

  login(){
    this.router.navigate(['/login']);
  }
}
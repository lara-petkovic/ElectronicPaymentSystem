import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'https://localhost:8085/api/authenticate'; 

  constructor(private http: HttpClient, private router: Router) { }

  authenticate(username: string, password: string): Observable<any> {
    const authRequest = { username, password }; 
    return this.http.post(`${this.baseUrl}/login`, authRequest, { observe: 'response' });
  }

  saveToken(token: string): void {
    localStorage.setItem('jwtToken', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/login']);
  }
}

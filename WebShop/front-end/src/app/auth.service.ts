import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from './components/models/user.model';
import { TokenStorage } from './components/auth/jwt/token.service';
import { AuthenticationResponse } from './components/models/authentication-response.model';
import { Login } from './components/models/login.model';
import { environment } from '../environments/environment.development';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from './services/user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user$ = new BehaviorSubject<User | null>(null);

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorage,
    private router: Router,
    private userService: UserService
  ) {
    this.loadUserFromToken();
  }

  private loadUserFromToken() {
    const token = this.tokenStorage.getAccessToken();
    if (token) {
      const jwtHelper = new JwtHelperService();
      try {
        const decodedToken = jwtHelper.decodeToken(token);
        const userId = decodedToken.id;
        this.userService.getUserById(userId).subscribe({
          next: (user: User) => {
            this.user$.next(user);
          },
          error: (error) => {
            console.error('Error fetching user data:', error);
          }
        });
      } catch (error) {
        console.error('Error decoding token:', error);
      }
    }
  }

  login(credentials: Login): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(`${environment.apiURL}/users/login`, credentials)
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.setUser(authenticationResponse.accessToken);
        })
      );
  }

  private setUser(accessToken: string): void {
    const jwtHelper = new JwtHelperService();
    try {
      const decodedToken = jwtHelper.decodeToken(accessToken);
      const userId = decodedToken.id;
      this.userService.getUserById(userId).subscribe({
        next: (user: User) => {
          this.user$.next(user);
        },
        error: (error) => {
          console.error('Error fetching user data:', error);
        }
      });
    } catch (error) {
      console.error('Error decoding token', error);
    }
  }

  logout(): void {
    this.router.navigate(['/login']).then(() => {
      this.tokenStorage.clear();
      this.user$.next(null);
    });
  }

  getAccessToken(): string | null {
    return this.tokenStorage.getAccessToken();
  }
}
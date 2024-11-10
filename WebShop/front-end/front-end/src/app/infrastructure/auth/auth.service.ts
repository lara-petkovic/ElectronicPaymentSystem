import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { TokenStorage } from './jwt/token.service';
import { environment } from 'src/env/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Login } from './model/login.model';
import { AuthenticationResponse } from './model/authentication-response.model';
import { User } from './model/user.model';
import { Registration } from './model/registration.model';
import { SecureToken } from './model/secure-token.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user$ = new BehaviorSubject<User>({id: 0,
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    password: '' });

  constructor(private http: HttpClient,
    private tokenStorage: TokenStorage,
    private router: Router) { }

  login(login: Login): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(environment.apiHost + '/users/login', login)
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.setUser();
        })
      );
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(environment.apiHost + '/users', user);
  }

  logout(): void {
    this.tokenStorage.clear();
    this.user$.value.id = 0;
    this.user$.next({
      id: 0,
      username: '',
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    });
    this.router.navigate(['/']).catch(error => console.error('Navigation error:', error));
  }

  checkIfUserExists(): void {
    const accessToken = this.tokenStorage.getAccessToken();
    if (accessToken == null) {
      this.user$.next({
        id: 0,
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        password: ''
      });
      return;
    }
    this.setUser();
  }

  private setUser(): void {
    const jwtHelperService = new JwtHelperService();
    const accessToken = this.tokenStorage.getAccessToken() || "";
    const user: User = {
      id: +jwtHelperService.decodeToken(accessToken).id,
      username: jwtHelperService.decodeToken(accessToken).username,
      firstName: jwtHelperService.decodeToken(accessToken).firstName,
      lastName: jwtHelperService.decodeToken(accessToken).lastName,
      password: jwtHelperService.decodeToken(accessToken).password,
      email: jwtHelperService.decodeToken(accessToken).email
    };
    this.user$.next(user);
  }
  
  getAccessToken(): string | null {
    const token = this.tokenStorage.getAccessToken(); 
    return token || null;
  }

  getSecureToken(username: string): Observable<SecureToken> {
    return this.http.get<SecureToken>(environment.apiHost + 'secureTokens/get-secure-token/' + username)
  }

  getUserIdByTokenData(tokenData: string): Observable<number> {
    return this.http.get<number>(environment.apiHost + 'secureTokens/get-user-id/' + tokenData)
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(environment.apiHost + 'users/' + userId);
  }
}
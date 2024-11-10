import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { User } from '../components/models/user.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  register(user: User): Observable<User> {
    return this.http.post<User>(environment.apiURL + '/users', user);
  }

  login(credentials: { username: string, password: string }): Observable<any> {
    return this.http.post<any>(`${environment.apiURL}/users/login`, credentials);
  }

  getUser(): Observable<User> {
    const token = localStorage.getItem('access_token');
    if (!token) {
      console.error("No token found in localStorage.");
      return throwError('Unauthorized: No token found');
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>(environment.apiURL + '/users/loggedInUser', { headers });
  }

  getUserById(userId: number): Observable<User> {
    const token = localStorage.getItem('access_token');
    if (!token) {
      console.error('No token found in localStorage.');
      return throwError('Unauthorized: No token found');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>(`${environment.apiURL}/users/${userId}`, { headers });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8085/api/authenticate'; 

  constructor(private http: HttpClient) { }

  authenticate(username: string, password: string): Observable<any> {
    const authRequest = { username, password }; 
    return this.http.post<any>(this.apiUrl, authRequest); 
  }
}

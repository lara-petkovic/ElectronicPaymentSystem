import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenStorage {
  private readonly ACCESS_TOKEN_KEY = 'access_token';

  saveAccessToken(token: string): void {
    localStorage.setItem(this.ACCESS_TOKEN_KEY, token);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  clear(): void {
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
  }
}
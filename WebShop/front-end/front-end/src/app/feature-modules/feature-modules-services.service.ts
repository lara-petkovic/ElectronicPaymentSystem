import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorage } from '../infrastructure/auth/jwt/token.service';
import { Observable } from 'rxjs';
import { Service } from '../infrastructure/auth/model/service.model';
import { environment } from 'src/env/environment';
import { Package } from '../infrastructure/auth/model/package.model';

@Injectable({
  providedIn: 'root'
})
export class FeatureModulesServicesService {

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorage,
    private router: Router
  ) { }

  getServices(): Observable<Service[]> {
    return this.http.get<Service[]>(`${environment.apiHost}/services`);
  }

  getPackages(): Observable<Package[]> {
    return this.http.get<Package[]>(`${environment.apiHost}/packages`);
  }
}

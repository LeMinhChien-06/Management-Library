import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/User';
import { ApiResponse } from '../models/ApiResponse';
import {StorageService} from './StorageService';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.hostBackend}`;

  constructor(
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  login(request: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/auth/login`, request)
      .pipe(
        tap(response => {
          if(response.data && response.data.token){
            this.setToken(response.data.token);
          }
        })
      )
  }

  setToken(token: string): void {
    this.storageService.setItem('token', token);
  }

  getToken(): string | null {
    return this.storageService.getItem('token');
  }

  logout(): Observable<ApiResponse<void>> {
    const token = this.getToken();
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/auth/logout`, { token })
      .pipe(
        tap(() => {
          this.storageService.removeItem('token');
        })
      );
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}

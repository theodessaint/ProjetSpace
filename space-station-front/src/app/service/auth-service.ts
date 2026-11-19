import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthRequest } from '../model/auth-request';
import { AuthResponse } from '../model/auth-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http: HttpClient = inject(HttpClient);
  private _token: string = sessionStorage.getItem("token") ?? "";
  private _role: string = sessionStorage.getItem("role") ?? "";
  private _username: string = sessionStorage.getItem("username") ?? "";

  public get token(): string {
    return this._token;
  }

  public set token(value: string) {
    this._token = value;
    sessionStorage.setItem("token", value);
  }

  public get role(): string {
    return this._role;
  }

  public set role(value: string) {
    this._role = value;
    sessionStorage.setItem("role", value);
  }

  public get username(): string {
    return this._username;
  }

  public set username(value: string) {
    this._username = value;
    sessionStorage.setItem("username", value);
  }

  public auth(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('/compte/auth', authRequest);
  }

  public isLogged() {
    return !!this._token;
  }

  public isAdmin() {
    return this._role === 'ADMIN';
  }

  public resetAuth() {
    this._token = "";
    this._role = "";
    this._username = "";
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("role");
    sessionStorage.removeItem("username");
  }
}

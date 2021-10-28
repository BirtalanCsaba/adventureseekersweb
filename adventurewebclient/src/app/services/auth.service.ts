import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { User } from '../common/entity/user.entity';
import { JwtHelperService } from '@auth0/angular-jwt';
import { TokenHelper } from './local-storage/token.helper';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API_URL = environment.API_URL;

  constructor(
    private http: HttpClient,
    private router: Router,
    private jwtHelper: JwtHelperService) { }

  /**
   * Logging in with the given credentials.
   * On successful login, adds the access and refresh tokens to the local storage
   * @param credentials The login credentials
   * @returns True on successful login, otherwise false.
   */
  login(credentials: any) {
    return this.http.post(this.API_URL + '/api/auth/login', 
      JSON.stringify(credentials))
      .pipe(map((response: any) => {
        let result = response;
        if (result && result.access_token && result.refresh_token) {
          localStorage.setItem('access_token', result.access_token);
          localStorage.setItem('refresh_token', result.refresh_token);
          return true;
        }
        return false;
      }))
      .pipe(catchError(error => {
        throw error;
      }));
  }

  /**
   * Loggs out the user by deleting the access tokens.
   */
  logout() {
    TokenHelper.removeAccessToken();
    TokenHelper.removeRefreshToken();
    this.router.navigate(["/"]);
  }

  /**
   * Checks whether the user is logged in.
   * @returns True if the user is logged in, otherwise false.
   */
  isLoggedIn() {
    return !this.jwtHelper.isTokenExpired();
  }

  /**
   * Getts the current user`s username.
   */
  get currentUser() {
    let token = TokenHelper.getAccessToken();
    if (!token) return null;

    return this.jwtHelper.decodeToken(token);
  }
  
  /**
   * Registers the new user
   * @param newUser The user to be registered
   */
  register(newUser: User) {
    return this.http.post(
      this.API_URL + "/api/auth/register", 
      JSON.stringify(newUser),
      { 
        headers: {'Content-Type':'application/json; charset=utf-8'}, 
        responseType: 'text' 
      })
      .pipe(map((response: any) => {
        let result = response;
        if (result) return true;
        else return false;
      }))
      .pipe(catchError(error => {
        throw error;
      }));
  }

  /**
   * Confirms the given token
   * @param token The token to be confirmed
   */
  confirmToken(token: string) {
    let httpParams = new HttpParams().append("token", token);
    return this.http.get(
      this.API_URL + "/api/auth/confirmation",
      {
        params: httpParams
      }
    )
    .pipe(map((response: any) => {
      let result = response;
      if (result) return true;
      else return false;
    }))
    .pipe(catchError(error => {
      console.log(error);
      throw error;
    }));
  }

  resendToken(email: string) {
    let httpParams = new HttpParams().append("email", email);
    return this.http.get(
      this.API_URL + "/api/auth/resend", 
      {
        params: httpParams
      }
    ).pipe(catchError(error => {
      console.log(error);
      throw error;
    }));
  }

}


import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

import { environment } from 'src/environments/environment';
import { endpoints } from 'src/enums/endpoints.enum';

import { IUser, User } from '../models/user.model';
import { ToastrService } from 'ngx-toastr';
import { RoleService } from './role.service';
import { IApiResponse, IAuthResponse } from '../models/api-response.model';
import { IJwtPayload } from '../models/jwt.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user = new BehaviorSubject(null);
  currentUserEmail: string | null = null;
  API_URL = environment.API_URL;
  TOKEN = null;
  REFRESH_TOKEN = null;

  constructor(
    private http: HttpClient,
    private toastr: ToastrService,
    private roleService: RoleService,
    private router: Router
  ) {}

  signIn(loginData: { email: string; password: string }) {
    const body = {
      username: loginData.email,
      password: loginData.password,
    };

    return this.http
      .post<IApiResponse<IAuthResponse>>(
        `${this.API_URL + endpoints.authenticate}`,
        body
      )
      .pipe(
        tap((resData) => {
          if (resData.statusCode === 200) {
            const accessToken = resData.data.token.accessToken;
            const refreshToken = resData.data.token.refreshToken;
            const decodedToken: IJwtPayload = jwtDecode(accessToken);
            const user = new User(
              decodedToken.sub,
              decodedToken.roles,
              accessToken,
              refreshToken
            );

            this.authorizeUser(user, accessToken, refreshToken);
          }
        })
      );
  }

  signUp(registerData: {
    firstname: string;
    lastname: string;
    email: string;
    password: string;
  }) {
    const body = {
      firstname: registerData.firstname,
      lastname: registerData.lastname,
      email: registerData.email,
      password: registerData.password,
    };

    return this.http
      .post<IApiResponse<IAuthResponse>>(
        `${this.API_URL + endpoints.register}`,
        body
      )
      .pipe(
        tap((resData) => {
          if (resData.statusCode === 200) {
            const accessToken = resData.data.token.accessToken;
            const refreshToken = resData.data.token.refreshToken;

            const decodedToken: IJwtPayload = jwtDecode(accessToken);

            const user = new User(
              decodedToken.sub,
              decodedToken.roles,
              accessToken,
              refreshToken
            );
            this.authorizeUser(user, accessToken, refreshToken);
          }
        })
      );
  }

  changePassword(passwordData: { oldPassword: string; newPassword: string }) {
    const body = {
      oldPassword: passwordData.oldPassword,
      newPassword: passwordData.newPassword,
    };

    return this.http.post(`${this.API_URL + endpoints.changePassword}`, body);
  }

  autoLogin() {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    this.TOKEN = accessToken;
    this.REFRESH_TOKEN = refreshToken;

    if (!accessToken) {
      return;
    }

    const decodedToken: IJwtPayload = jwtDecode(accessToken);

    // TOKEN DEBUG
    // console.log('Saved token: ', saveToken);
    // console.log('Decoded token: ', decodedToken);
    // console.log('Created time: ', new Date(decodedToken.iat * 1000));
    // console.log('Expired time: ', new Date(decodedToken.exp * 1000));
    // console.log('Current time: ', new Date());

    const tokenIsValid = this.tokenIsValid(accessToken);
    if (accessToken && tokenIsValid) {
      const user = new User(
        decodedToken.sub,
        decodedToken.roles,
        accessToken,
        refreshToken
      );
      this.authorizeUser(user, accessToken, refreshToken);
    } else if (this.tokenIsValid(refreshToken)) {
      this.refreshToken().subscribe({
        next: (res) => {
          // console.log(res);
        },
        error: (err) => {
          console.error(err);
        },
      });
    }
  }

  tokenIsValid(token: string) {
    const decodedToken: IJwtPayload = jwtDecode(token);
    const validationResult = decodedToken.exp * 1000 > new Date().getTime();
    return validationResult;
  }

  signOut() {
    this.user.next(null);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.roleService.setRoles([]);
    this.router.navigate(['/']);

    this.toastr.info('Wylogowano pomyślnie!', null, {
      positionClass: 'toast-bottom-right',
    });
  }

  refreshToken() {
    return this.http
      .post<IApiResponse<IAuthResponse>>(
        `${this.API_URL + endpoints.tokenRefresh}`,
        {},
        {
          headers: {
            skipInterceptor: 'true',
            Authorization: `Bearer ${this.REFRESH_TOKEN}`,
          },
        }
      )
      .pipe(
        tap((res) => {
          const accessToken = res.data.token.accessToken;
          const refreshToken = res.data.token.refreshToken;
          const decodedToken: IJwtPayload = jwtDecode(accessToken);
          const user = new User(
            decodedToken.sub,
            decodedToken.roles,
            accessToken,
            refreshToken
          );

          this.authorizeUser(user, accessToken, refreshToken);
        })
      );
  }

  authorizeUser(user: IUser, accessToken: string, refreshToken: string): void {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    this.user.next(user);
    this.roleService.setRoles([user.role]);
    this.currentUserEmail = user.email;
  }
}

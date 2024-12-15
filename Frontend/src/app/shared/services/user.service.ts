import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IAddressResponse } from '../models/address.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  API_URL = environment.API_URL;

  dummyUserData = {
    name: 'John',
    surname: 'Doe',
    email: 'test@mail.com',
    phone: '123456789',
  };
  dummyAddressData = {
    city: 'Warsaw',
    street: 'Marszałkowska',
    number: '1',
    zip: '00-000',
  };

  constructor(private http: HttpClient) {}

  getUserData(): Observable<any> {
    return of(this.dummyUserData);
  }

  updateUserData() {}

  getUserAddress(): Observable<any> {
    return this.http.get<IAddressResponse>(`${this.API_URL}address/1`);
  }
}

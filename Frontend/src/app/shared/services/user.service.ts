import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { first, last, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IAddress } from '../models/address.model';
import { IApiResponse } from '../models/api-response.model';
import { IUserDetails } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  API_URL = environment.API_URL;

  // dummyUserData = {
  //   name: 'John',
  //   surname: 'Doe',
  //   email: 'test@mail.com',
  //   phone: '123456789',
  // };
  // dummyAddressData = {
  //   city: 'Warsaw',
  //   street: 'Marszałkowska',
  //   number: '1',
  //   zip: '00-000',
  // };

  constructor(private http: HttpClient) {}

  getUserData(): Observable<any> {
    // return of(this.dummyUserData);
    return this.http.get<IApiResponse<IUserDetails>>(`${this.API_URL}appUser`);
  }

  updateUserData(userData: any) {}

  getUserAddress(): Observable<any> {
    return this.http.get<IApiResponse<IAddress>>(`${this.API_URL}address/1`);
  }

  addNewAddress(userAddress: IAddress) {
    const newAddress = {
      firstName: userAddress.firstName,
      lastName: userAddress.lastName,
      phoneNumber: userAddress.phoneNumber,
      street: userAddress.street,
      zipCode: userAddress.zipCode,
      city: userAddress.city,
    };

    return this.http.post(`${this.API_URL}address`, newAddress);
  }

  updateAddress(userAddress: IAddress) {
    console.log(userAddress);
    const newAddress = {
      firstName: userAddress.firstName,
      lastName: userAddress.lastName,
      phoneNumber: userAddress.phoneNumber,
      street: userAddress.street,
      zipCode: userAddress.zipCode,
      city: userAddress.city,
    };

    // console.log(userAddress);
    // console.log(newAddress);

    // console.log(newAddress);
    return this.http.put(`${this.API_URL}address/1`, newAddress);
  }

  changePassword(passwords: any) {
    console.log(passwords);
  }
}

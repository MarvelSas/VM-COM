import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { first, last, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IAddress } from '../models/address.model';
import { IApiResponse } from '../models/api-response.model';
import { IUserDetails } from '../models/user.model';
import { endpoints } from 'src/enums/endpoints.enum';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  API_URL = environment.API_URL;

  constructor(private http: HttpClient) {}

  getUserData(): Observable<any> {
    return this.http.get<IApiResponse<IUserDetails>>(
      `${this.API_URL + endpoints.getUser}`
    );
  }

  updateUserData(userData: IUserDetails) {
    return this.http.patch(`${this.API_URL + endpoints.updateUser}`, userData);
  }

  getUserAddresses(): Observable<any> {
    return this.http.get<IApiResponse<IAddress>>(
      `${this.API_URL + endpoints.getUserAddresses}`
    );
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

    return this.http.post(
      `${this.API_URL + endpoints.addUserAddress}`,
      newAddress
    );
  }

  updateAddress(userAddress: IAddress, id: number) {
    const newAddress = {
      firstName: userAddress.firstName,
      lastName: userAddress.lastName,
      phoneNumber: userAddress.phoneNumber,
      street: userAddress.street,
      zipCode: userAddress.zipCode,
      city: userAddress.city,
    };

    return this.http.put(
      `${this.API_URL + endpoints.updateAddress + id}`,
      newAddress
    );
  }
}

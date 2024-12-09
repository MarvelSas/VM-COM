import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
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

  constructor() {}

  getUserData(): Observable<any> {
    return of(this.dummyUserData);
  }

  updateUserData() {}

  getUserAddress(): Observable<any> {
    return of(this.dummyAddressData);
  }
}

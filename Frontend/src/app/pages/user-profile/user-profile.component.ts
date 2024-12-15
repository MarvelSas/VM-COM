import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IAddressResponse } from 'src/app/shared/models/address.model';
import { UserService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  userForm: FormGroup;
  editMode = false;
  userData: any = {};
  userAddress: IAddressResponse;
  addressNotFound = false;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      shipFirstName: ['', Validators.required],
      shipLastName: ['', Validators.required],
      shipStreet: ['', Validators.required],
      shipCity: ['', Validators.required],
      shipZipCode: ['', Validators.required],
      shipPhoneNumber: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.getUserData();
  }

  getUserData() {
    this.userService.getUserData().subscribe({
      next: (data) => {
        this.userData = data;
        this.userForm.patchValue({
          firstName: data.name,
          lastName: data.surname,
          email: data.email,
          phone: data.phone,
        });
      },
      error: (error) => {
        console.error('Error fetching user data:', error);
      },
      complete: () => {},
    });

    this.userService.getUserAddress().subscribe({
      next: (data: IAddressResponse) => {
        this.userAddress = data;
        this.userForm.patchValue({
          shipFirstName: data.data.data.firstName,
          shipLastName: data.data.data.lastName,
          shipStreet: data.data.data.street,
          shipCity: data.data.data.city,
          shipZipCode: data.data.data.zipCode,
          shipPhoneNumber: data.data.data.phoneNumber,
        });

        console.log(data.data.data.firstName);
        this.addressNotFound = false;
      },
      error: (error) => {
        if (error.status === 400) {
          this.addressNotFound = true;
        } else {
          console.error('Error fetching user address:', error);
        }
      },
    });
  }

  toggleEdit() {
    this.editMode = !this.editMode;
  }

  saveChanges() {
    if (this.userForm.valid) {
      console.log('User data saved:', this.userForm.value);
      this.editMode = false;
    } else {
      console.error('Invalid form data');
    }
  }
}

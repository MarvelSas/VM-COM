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
  addressForm: FormGroup;
  passwordForm: FormGroup;
  editModeUser = false;
  editModeAddress = false;
  editModePassword = false;
  userData: any = {};
  addressData: any = {};
  userAddress: IAddressResponse;
  addressNotFound = false;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
    });

    this.addressForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      street: ['', Validators.required],
      city: ['', Validators.required],
      zipCode: ['', Validators.required],
      phoneNumber: ['', Validators.required],
    });

    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.getUserData();
    this.getAddresses();
  }

  getUserData() {
    this.userService.getUserData().subscribe({
      next: (res) => {
        this.userData = res;
        this.userForm.patchValue({
          firstName: res.name,
          lastName: res.surname,
          email: res.email,
          phone: res.phone,
        });
      },
      error: (error) => {
        console.error('Error fetching user data:', error);
      },
      complete: () => {},
    });
  }

  getAddresses() {
    this.userService.getUserAddress().subscribe({
      next: (res: IAddressResponse) => {
        this.addressData = res;
        this.addressForm.patchValue({
          firstName: res.data.data.firstName,
          lastName: res.data.data.lastName,
          street: res.data.data.street,
          city: res.data.data.city,
          zipCode: res.data.data.zipCode,
          phoneNumber: res.data.data.phoneNumber,
        });
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
  toggleEditUser() {
    this.editModeUser = !this.editModeUser;
  }

  toggleEditAdress() {
    this.editModeAddress = !this.editModeAddress;
  }

  toggleEditPassword() {
    this.editModePassword = !this.editModePassword;
  }

  saveUserChanges() {
    if (this.userForm.valid) {
      this.editModeUser = false;
      // console.log('User data saved:', this.userForm.value);
    }
  }

  saveAddressChanges() {
    if (this.addressForm.valid) {
      this.editModeAddress = false;
      console.log('Address data saved:', this.addressForm.value);
      if (this.addressNotFound) {
        console.log('Dodawanie nowego adresu');
        this.userService.addNewAddress(this.addressForm.value).subscribe({
          next: (res) => {
            // console.log('User address saved:', res);
          },
          error: (error) => {
            console.error('Error saving user data:', error);
          },
          complete: () => {
            // this.getAddresses();
          },
        });
      } else {
        console.log('Aktualizacja adresu');
        this.userService.updateAddress(this.addressForm.value).subscribe({
          next: (res) => {
            // console.log('User address saved:', res);
          },
          error: (error) => {
            console.error('Error saving user data:', error);
          },
          complete: () => {},
        });
      }
    } else {
      console.error('Invalid form data');
    }
  }

  savePasswordChanges() {
    this.userService.changePassword(this.passwordForm.value);
  }
}

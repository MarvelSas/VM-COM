import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/shared/services/user.service';
import { IAddress, IAddressData } from 'src/app/shared/models/address.model';
import { IApiResponse } from 'src/app/shared/models/api-response.model';
import { IUserDetails } from 'src/app/shared/models/user.model';

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
  addNewAddressMode = false;
  editModePassword = false;
  userData: any = {};
  addresses: IAddress[] = [];
  selectedAddressIndex: number = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
    });

    this.addressForm = this.fb.group({
      id: [null],
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
      next: (res: IApiResponse<IUserDetails>) => {
        this.userData = res;
        console.log(this.userData);
        this.userForm.patchValue({
          firstName:
            res.data.appUserDetails.firstName === ''
              ? 'Brak'
              : res.data.appUserDetails.firstName,
          lastName:
            res.data.appUserDetails.lastName === ''
              ? 'Brak'
              : res.data.appUserDetails.lastName,
          email:
            this.authService.currentUserEmail === ''
              ? 'Brak'
              : this.authService.currentUserEmail,
          phoneNumber:
            res.data.appUserDetails.phoneNumber === ''
              ? 'Brak'
              : res.data.appUserDetails.phoneNumber,
        });
      },
      error: (error) => {
        console.error('Error fetching user data:', error);
      },
      complete: () => {},
    });
  }

  getAddresses() {
    this.userService.getUserAddresses().subscribe({
      next: (res: IApiResponse<IAddressData>) => {
        this.addresses = res.data.data;
      },
      error: (error) => {
        console.error('Error fetching user addresses:', error);
      },
    });
  }

  saveUserChanges() {
    if (this.userForm.valid) {
      this.editModeUser = false;

      this.userService.updateUserData(this.userForm.value).subscribe({
        next: (res) => {},
        error: (error) => {
          console.error('Error saving user data:', error);
        },
        complete: () => {
          // this.getUserData();
        },
      });
    }
  }

  savePasswordChanges() {
    if (this.passwordForm.valid) {
    }
  }

  saveAddressChanges() {
    if (this.addressForm.valid) {
      const address = this.addressForm.value;
      if (this.selectedAddressIndex !== null) {
        this.addresses[this.selectedAddressIndex] = address;
      } else {
        this.addresses.push(address);
      }

      if (!this.addNewAddressMode) {
        this.userService
          .updateAddress(
            this.addressForm.value,
            this.addresses[this.selectedAddressIndex].id
          )
          .subscribe({
            next: (res) => {},
            error: (error) => {
              console.error('Error saving user data:', error);
            },
            complete: () => {
              this.editModeAddress = false;
              this.selectedAddressIndex = null;
              this.addressForm.reset();
              this.getAddresses();
            },
          });
      } else {
        this.userService.addNewAddress(address).subscribe({
          next: (res) => {
            this.editModeAddress = false;
            this.selectedAddressIndex = null;
            this.getAddresses();
          },
          error: (error) => {
            console.error('Error saving user address:', error);
          },
          complete: () => {
            this.addNewAddressMode = null;
            this.editModeAddress = false;
            this.addressForm.reset();
          },
        });
      }

      // this.userService.updateUserAddresses(this.addresses).subscribe({
      //   next: (res) => {
      //     this.editModeAddress = false;
      //     this.selectedAddressIndex = null;
      //   },
      //   error: (error) => {
      //     console.error('Error saving user address:', error);
      //   },
      // });
    } else {
      console.error('Invalid form data');
    }
  }

  toggleEditUser(): void {
    this.editModeUser = !this.editModeUser;
  }

  toggleEditPassword(): void {
    this.editModePassword = !this.editModePassword;
  }

  addNewAddressButton(): void {
    console.log('Add new address');
    this.addNewAddressMode = true;
    this.editModeAddress = true;
    this.addressForm.reset();
  }

  selectAddress(index: number): void {
    this.addNewAddressMode = false;
    this.selectedAddressIndex = index;
    this.addressForm.patchValue(this.addresses[index]);
    this.editModeAddress = true;
  }

  addNewAddress(): void {
    this.selectedAddressIndex = null;
    this.addressForm.reset();
    this.editModeAddress = true;
  }

  cancelAddressForm(): void {
    this.editModeAddress = false;
    this.selectedAddressIndex = null;
    this.addNewAddressMode = false;
    this.addressForm.reset();
  }
}

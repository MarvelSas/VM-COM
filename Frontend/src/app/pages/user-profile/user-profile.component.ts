import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
  userAddress: any = {};

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      address: ['', Validators.required],
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
      next: (data) => {
        this.userAddress = data;
        this.userForm.patchValue({
          address: `${data.street} ${data.number}, ${data.city}, ${data.zip}`,
        });
      },
      error: (error) => {
        console.error('Error fetching user address:', error);
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

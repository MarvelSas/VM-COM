import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

interface Order {
  id: number;
  date: string;
  status: string;
}

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent {
  userForm: FormGroup;
  editMode = false;

  constructor(private fb: FormBuilder) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      address: ['', Validators.required],
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

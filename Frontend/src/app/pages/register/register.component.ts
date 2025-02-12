import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  signUpForm: FormGroup;

  constructor(
    private authService: AuthService,
    private router: Router,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.signUpForm = new FormGroup({
      firstname: new FormControl(null, Validators.required),
      lastname: new FormControl(null, Validators.required),
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, [
        Validators.required,
        Validators.minLength(8),
      ]),
    });
  }

  onRegisterHandler() {
    if (this.signUpForm.valid) {
      this.authService.signUp(this.signUpForm.value).subscribe({
        next: (res) => {
          this.toast.success('Zarejestrowano pomyślnie!', null, {
            positionClass: 'toast-bottom-right',
          });
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error(err.message);
          if (err.status === 400) {
            this.toast.error('Błąd podczas rejestracji!', null, {
              positionClass: 'toast-bottom-right',
            });
          }
        },
      });
    }
  }

  //     email: 'marvel@gmail.com',
  //     password: 'test123',
}

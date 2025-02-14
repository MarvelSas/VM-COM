import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss'],
})
export class ErrorComponent implements OnInit {
  errorCode: number;
  errorMessage: string;
  errorDescription: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.errorCode = +params['code'] || 404;
      this.setErrorMessage();
      console.error(this.errorCode);
    });
  }

  setErrorMessage(): void {
    switch (this.errorCode) {
      case 401:
        this.errorMessage = 'Nieautoryzowany';
        this.errorDescription =
          'Nie masz uprawnień do przeglądania tej strony.';
        break;
      case 403:
        this.errorMessage = 'Zabronione';
        this.errorDescription = 'Nie masz dostępu do tej strony.';
        break;
      case 404:
      default:
        this.errorMessage = 'Strona nie została znaleziona';
        this.errorDescription =
          'Przepraszamy, ale strona, której szukasz, nie istnieje.';
        break;
    }
  }
}

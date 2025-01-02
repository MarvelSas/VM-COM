import { Component } from '@angular/core';

@Component({
  selector: 'app-help-user',
  templateUrl: './help-user.component.html',
  styleUrls: ['./help-user.component.scss'],
})
export class HelpUserComponent {
  answers: boolean[] = [];

  toggleAnswer(index: number): void {
    this.answers[index] = !this.answers[index];
  }
}

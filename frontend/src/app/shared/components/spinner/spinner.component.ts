import { Component, Input } from '@angular/core';

export type SpinnerSize = 'sm' | 'md' | 'lg';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent {
  @Input() size: SpinnerSize = 'md';
  @Input() color: 'primary' | 'secondary' | 'white' = 'primary';
  @Input() overlay = false;

  get spinnerClasses(): string {
    return `spinner spinner-${this.size} spinner-${this.color}`;
  }
}

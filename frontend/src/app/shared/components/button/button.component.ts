import { Component, Input, Output, EventEmitter } from '@angular/core';

export type ButtonVariant = 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger';
export type ButtonSize = 'sm' | 'md' | 'lg';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() variant: ButtonVariant = 'primary';
  @Input() size: ButtonSize = 'md';
  @Input() disabled = false;
  @Input() loading = false;
  @Input() fullWidth = false;
  @Input() type: 'button' | 'submit' | 'reset' = 'button';

  @Output() clicked = new EventEmitter<MouseEvent>();

  get buttonClasses(): string {
    const classes = ['btn', `btn-${this.variant}`, `btn-${this.size}`];
    if (this.fullWidth) {
      classes.push('btn-full');
    }
    if (this.loading) {
      classes.push('btn-loading');
    }
    return classes.join(' ');
  }

  onClick(event: MouseEvent): void {
    if (!this.disabled && !this.loading) {
      this.clicked.emit(event);
    }
  }
}

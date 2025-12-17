import { Component, Input, Output, EventEmitter } from '@angular/core';

export type AlertType = 'success' | 'error' | 'warning' | 'info';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent {
  @Input() type: AlertType = 'info';
  @Input() title?: string;
  @Input() message = '';
  @Input() dismissible = false;

  @Output() dismissed = new EventEmitter<void>();

  visible = true;

  get alertClasses(): string {
    return `alert alert-${this.type}`;
  }

  get icon(): string {
    const icons: Record<AlertType, string> = {
      success: '✓',
      error: '✕',
      warning: '⚠',
      info: 'ℹ'
    };
    return icons[this.type];
  }

  dismiss(): void {
    this.visible = false;
    this.dismissed.emit();
  }
}

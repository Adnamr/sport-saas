import { Component, ContentChild, Input, TemplateRef } from '@angular/core';

export type CardVariant = 'default' | 'elevated' | 'outlined';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent {
  @Input() variant: CardVariant = 'default';
  @Input() padding: 'none' | 'sm' | 'md' | 'lg' = 'md';
  @Input() hoverable = false;
  @Input() header?: string;
  @Input() footer?: string;

  get cardClasses(): string {
    const classes = ['card', `card-${this.variant}`, `card-padding-${this.padding}`];
    if (this.hoverable) {
      classes.push('card-hoverable');
    }
    return classes.join(' ');
  }
}

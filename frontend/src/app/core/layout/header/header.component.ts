import { Component, Output, EventEmitter, Input } from '@angular/core';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Input() sidebarOpen = true;
  @Output() toggleSidebar = new EventEmitter<void>();

  appName = environment.appName;

  onToggleSidebar(): void {
    this.toggleSidebar.emit();
  }
}

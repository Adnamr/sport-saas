import { Component, Input } from '@angular/core';

export interface NavItem {
  label: string;
  icon: string;
  route: string;
  children?: NavItem[];
}

@Component({
  selector: 'app-sidebar',
  standalone: false,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  @Input() isOpen = true;

  navItems: NavItem[] = [
    { label: 'Tableau de bord', icon: '📊', route: '/dashboard' },
    { label: 'Catalogue', icon: '📦', route: '/catalog' },
    { label: 'Inventaire', icon: '📋', route: '/inventory' },
    { label: 'Commandes', icon: '🛒', route: '/orders' },
    { label: 'Clients', icon: '👥', route: '/customers' },
    { label: 'Facturation', icon: '💰', route: '/billing' },
    { label: 'Rapports', icon: '📈', route: '/reports' },
    { label: 'Paramètres', icon: '⚙️', route: '/settings' }
  ];
}

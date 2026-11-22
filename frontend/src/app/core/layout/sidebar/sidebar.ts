import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Auth } from '../../services/auth';
import { MatIconModule } from '@angular/material/icon';

@Component({
  imports: [RouterLink, RouterLinkActive, MatIconModule],
  selector: 'app-sidebar',
  styleUrl: './sidebar.scss',
  templateUrl: './sidebar.html',
})
export class Sidebar { readonly auth = inject(Auth); readonly menus = { ADMIN: [{ label: 'Tableau de bord', icon: 'space_dashboard', link: '/admin/dashboard' }], GESTIONNAIRE: [{ label: 'Tableau de bord', icon: 'space_dashboard', link: '/admin/dashboard' }], LIVREUR: [{ label: 'Mes tournées', icon: 'route', link: '/driver/my-tours' }], CLIENT: [{ label: 'Mes colis', icon: 'inventory_2', link: '/client/orders' }] }; }

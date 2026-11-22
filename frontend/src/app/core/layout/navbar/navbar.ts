import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  imports: [MatIconModule, MatButtonModule],
  selector: 'app-navbar',
  styleUrl: './navbar.scss',
  templateUrl: './navbar.html',
})
export class Navbar { readonly auth = inject(Auth); private readonly router = inject(Router); logout(): void { this.auth.logout(); void this.router.navigate(['/login']); } }

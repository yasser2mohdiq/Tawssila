import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({ selector: 'app-home-redirect', template: '' })
export class HomeRedirect implements OnInit {
  private readonly auth = inject(Auth);
  private readonly router = inject(Router);

  ngOnInit(): void {
    const user = this.auth.user();
    void this.router.navigateByUrl(user ? this.auth.redirectForRole(user.role) : '/login');
  }
}

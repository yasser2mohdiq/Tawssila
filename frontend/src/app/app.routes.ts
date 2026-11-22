import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/auth/login/login').then(m => m.Login) },
  { path: 'register', loadComponent: () => import('./features/auth/register/register').then(m => m.Register) },
  { path: 'forgot-password', loadComponent: () => import('./features/auth/forgot-password/forgot-password').then(m => m.ForgotPassword) },
  { path: 'tracking', loadComponent: () => import('./features/tracking/public-tracking/public-tracking').then(m => m.PublicTracking) },
  { path: '', canActivate: [authGuard], loadComponent: () => import('./core/layout/main-layout/main-layout').then(m => m.MainLayout), children: [
    { path: 'admin/dashboard', canActivate: [roleGuard], data: { roles: ['ADMIN', 'GESTIONNAIRE'] }, loadComponent: () => import('./features/admin/dashboard/dashboard').then(m => m.Dashboard) },
    { path: 'driver/my-tours', canActivate: [roleGuard], data: { roles: ['LIVREUR'] }, loadComponent: () => import('./features/driver/my-tours/my-tours').then(m => m.MyTours) },
    { path: 'client/orders', canActivate: [roleGuard], data: { roles: ['CLIENT'] }, loadComponent: () => import('./features/client/orders/orders').then(m => m.Orders) },
    { path: '', pathMatch: 'full', loadComponent: () => import('./core/layout/home-redirect/home-redirect').then(m => m.HomeRedirect) }
  ] },
  { path: '**', loadComponent: () => import('./features/not-found/not-found').then(m => m.NotFound) }
];

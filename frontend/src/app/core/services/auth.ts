import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, delay, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User, UserRole } from '../models/user';

@Injectable({ providedIn: 'root' })
export class Auth {
  private readonly storageKey = 'tawssila.user';
  private readonly currentUser = signal<User | null>(this.readUser());
  readonly user = this.currentUser.asReadonly();
  readonly isAuthenticated = computed(() => this.currentUser() !== null);
  private readonly http = inject(HttpClient);
  login(email: string, password: string): Observable<User> {
    if (!environment.USE_MOCK) return this.http.post<User>(`${environment.apiUrl}/auth/login`, { email, password }).pipe(tap(user => this.saveUser(user)));
    const role: UserRole = email.toLowerCase().includes('admin') ? 'ADMIN' : email.toLowerCase().includes('gestion') ? 'GESTIONNAIRE' : email.toLowerCase().includes('livreur') ? 'LIVREUR' : 'CLIENT';
    const user: User = { id: `mock-${role.toLowerCase()}`, nom: email.split('@')[0] || 'Utilisateur', email, role };
    return of(user).pipe(delay(450), tap(value => this.saveUser(value)));
  }
  register(input: { nom: string; email: string; password: string }): Observable<User> {
    if (!environment.USE_MOCK) return this.http.post<User>(`${environment.apiUrl}/auth/register`, input).pipe(tap(user => this.saveUser(user)));
    const user: User = { id: `mock-${Date.now()}`, nom: input.nom, email: input.email, role: 'CLIENT' };
    return of(user).pipe(delay(450), tap(value => this.saveUser(value)));
  }
  forgotPassword(email: string): Observable<void> {
    if (!environment.USE_MOCK) return this.http.post<void>(`${environment.apiUrl}/auth/forgot-password`, { email });
    return of(void 0).pipe(delay(450));
  }
  logout(): void { localStorage.removeItem(this.storageKey); localStorage.removeItem('tawssila.token'); this.currentUser.set(null); }
  redirectForRole(role: UserRole): string { return role === 'ADMIN' || role === 'GESTIONNAIRE' ? '/admin/dashboard' : role === 'LIVREUR' ? '/driver/my-tours' : '/client/orders'; }
  private saveUser(user: User): void { localStorage.setItem(this.storageKey, JSON.stringify(user)); this.currentUser.set(user); }
  private readUser(): User | null {
    try { const value: unknown = JSON.parse(localStorage.getItem(this.storageKey) ?? 'null');
      if (typeof value === 'object' && value !== null && 'role' in value && 'email' in value && 'nom' in value && 'id' in value) return value as User;
    } catch { return null; }
    return null;
  }
}

import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { Auth } from '../../../core/services/auth';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  imports: [ReactiveFormsModule, RouterLink, MatIconModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  selector: 'app-login',
  styleUrl: './login.scss',
  templateUrl: './login.html',
})
export class Login { private readonly fb = inject(FormBuilder); private readonly auth = inject(Auth); private readonly router = inject(Router); private readonly route = inject(ActivatedRoute); readonly loading = signal(false); readonly error = signal(''); readonly form = this.fb.nonNullable.group({ email: ['', [Validators.required, Validators.email]], password: ['', [Validators.required, Validators.minLength(6)]] }); submit(): void { if (this.form.invalid) { this.form.markAllAsTouched(); return; } this.loading.set(true); this.error.set(''); this.auth.login(this.form.getRawValue().email, this.form.getRawValue().password).subscribe({ next: user => { const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl'); void this.router.navigateByUrl(returnUrl?.startsWith('/') ? returnUrl : this.auth.redirectForRole(user.role)); }, error: () => { this.loading.set(false); this.error.set('Identifiants incorrects. Veuillez réessayer.'); } }); } }

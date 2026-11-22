import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Auth } from '../../../core/services/auth';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({ selector: 'app-forgot-password', imports: [ReactiveFormsModule, RouterLink, MatIconModule, MatButtonModule, MatFormFieldModule, MatInputModule], templateUrl: './forgot-password.html', styleUrl: './forgot-password.scss' })
export class ForgotPassword {
  private readonly fb = inject(FormBuilder); private readonly auth = inject(Auth);
  readonly done = signal(false); readonly loading = signal(false);
  readonly form = this.fb.nonNullable.group({ email: ['', [Validators.required, Validators.email]] });
  submit(): void { if (this.form.invalid) { this.form.markAllAsTouched(); return; } this.loading.set(true); this.auth.forgotPassword(this.form.controls.email.value).subscribe({ next: () => { this.loading.set(false); this.done.set(true); }, error: () => this.loading.set(false) }); }
}

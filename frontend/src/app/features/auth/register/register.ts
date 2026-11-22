import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../../core/services/auth';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  imports: [ReactiveFormsModule, RouterLink, MatIconModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  selector: 'app-register',
  styleUrl: './register.scss',
  templateUrl: './register.html',
})
export class Register { private readonly fb = inject(FormBuilder); private readonly auth = inject(Auth); private readonly router = inject(Router); readonly loading = signal(false); readonly error = signal(''); readonly form = this.fb.nonNullable.group({ nom: ['', [Validators.required, Validators.minLength(2)]], email: ['', [Validators.required, Validators.email]], telephone: ['', [Validators.required, Validators.pattern(/^[+\d\s()-]{8,}$/)]], password: ['', [Validators.required, Validators.minLength(8)]] }); submit(): void { if (this.form.invalid) { this.form.markAllAsTouched(); return; } this.loading.set(true); this.auth.register({ nom: this.form.controls.nom.value, email: this.form.controls.email.value, password: this.form.controls.password.value }).subscribe({ next: () => void this.router.navigate(['/client/orders']), error: () => { this.loading.set(false); this.error.set('La création du compte a échoué. Réessayez.'); } }); } }

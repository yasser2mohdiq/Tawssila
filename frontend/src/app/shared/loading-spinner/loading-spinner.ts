import { Component, input } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
@Component({ selector: 'app-loading-spinner', imports: [MatProgressSpinnerModule], template: '<div class="loading" role="status"><mat-spinner [diameter]="diameter()"/><span>{{ label() }}</span></div>', styles: [':host{display:block}.loading{min-height:120px;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:12px;color:#718096}'] })
export class LoadingSpinner { readonly diameter = input(36); readonly label = input('Chargement…'); }

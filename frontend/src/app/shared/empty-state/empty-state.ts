import { Component, input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
@Component({ selector: 'app-empty-state', imports: [MatIconModule], template: '<div class="empty"><mat-icon>{{ icon() }}</mat-icon><h3>{{ title() }}</h3><p>{{ message() }}</p><ng-content /></div>', styles: [':host{display:block}.empty{text-align:center;padding:44px 20px;color:#718096}.empty mat-icon{font-size:40px;width:40px;height:40px;color:#93a5b8}h3{color:#263b54;margin:12px 0 6px}p{margin:0}'] })
export class EmptyState { readonly icon = input('inventory_2'); readonly title = input('Aucun résultat'); readonly message = input('Les éléments apparaîtront ici.'); }

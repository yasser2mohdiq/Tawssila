import { Component, input } from '@angular/core';
@Component({ selector: 'app-page-header', template: '<header class="page-header"><div><h1>{{ title() }}</h1><p>{{ subtitle() }}</p></div><ng-content /></header>', styles: [':host{display:block}.page-header{display:flex;justify-content:space-between;align-items:center;gap:16px;margin-bottom:26px}h1{font-size:28px;color:#102b4e;margin:0 0 5px}p{margin:0;color:#718096}'] })
export class PageHeader { readonly title = input.required<string>(); readonly subtitle = input(''); }

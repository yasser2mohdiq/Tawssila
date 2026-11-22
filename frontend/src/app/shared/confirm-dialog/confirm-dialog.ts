import { Component, inject, input } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
@Component({ selector: 'app-confirm-dialog', imports: [MatDialogModule, MatButtonModule], template: '<h2 mat-dialog-title>{{ title() }}</h2><mat-dialog-content>{{ message() }}</mat-dialog-content><mat-dialog-actions align="end"><button mat-button (click)="close(false)">Annuler</button><button mat-flat-button color="primary" (click)="close(true)">{{ confirmLabel() }}</button></mat-dialog-actions>' })
export class ConfirmDialog { private readonly ref = inject(MatDialogRef<ConfirmDialog, boolean>); readonly title = input('Confirmer'); readonly message = input('Souhaitez-vous continuer ?'); readonly confirmLabel = input('Confirmer'); close(value: boolean): void { this.ref.close(value); } }

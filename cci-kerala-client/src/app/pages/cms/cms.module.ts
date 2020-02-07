import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CmsRoutingModule } from './cms-routing.module';
import { NewsUpdateComponent } from './news-update/news-update.component';
import { MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule, MatIconModule, MatExpansionModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TableModule } from 'lib-table/public_api';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';

@NgModule({
  declarations: [NewsUpdateComponent, ConfirmationDialogComponent],
  imports: [
    CommonModule,
    CmsRoutingModule,
    MatCardModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    TableModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatIconModule,
    MatExpansionModule
  ],
  entryComponents: [ConfirmationDialogComponent],
  providers: [
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: true } }
  ]
})
export class CmsModule { }

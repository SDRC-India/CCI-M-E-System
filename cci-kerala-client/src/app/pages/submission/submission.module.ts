import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SubmissionRoutingModule } from './submission-routing.module';
import { InstitutionManagementComponent } from './institution-management/institution-management.component';
import { InmatesManagementComponent } from './inmates-management/inmates-management.component';
import { ViewDetailsComponent } from './view-details/view-details.component';
import { MatCardModule, MatTooltipModule, MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatDialogModule, MatInputModule, MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material';
import { TableModule } from 'lib-table/public_api';
import { FormModule } from 'lib/public_api';
import { FormsModule } from '@angular/forms';
import { RejectionReasonComponent } from './dialog/rejection-reason/rejection-reason.component';

@NgModule({
  declarations: [InstitutionManagementComponent, InmatesManagementComponent, ViewDetailsComponent, RejectionReasonComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    MatCardModule,
    TableModule,
    MatTooltipModule,
    MatButtonModule,
    MatCheckboxModule,
    SubmissionRoutingModule,
    FormModule,
    FormsModule
  ],
  entryComponents: [RejectionReasonComponent],
  providers: [
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: true } }
  ]
})
export class SubmissionModule { }

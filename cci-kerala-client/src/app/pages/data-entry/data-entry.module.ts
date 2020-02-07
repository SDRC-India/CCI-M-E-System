import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataEntryRoutingModule } from './data-entry-routing.module';
import { InstitutionInmatesComponent } from './institution-inmates/institution-inmates.component';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { FormModule } from 'lib/public_api';
import { TableModule } from 'lib-table/public_api';
import { MatCardModule, MatTabsModule, MatButtonModule, MatTooltipModule, MatToolbarModule, MatIconModule, MatSidenavContainer, MatSidenavModule, MatNavList, MatListModule, MatSlideToggleModule, MatDialogModule, MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material';
import { InmatesHomeComponent } from './inmates-home/inmates-home.component';
import { CciHomeComponent } from './cci-home/cci-home.component';
import { InstitutionInmatesViewComponent } from './institution-inmates-view/institution-inmates-view.component';
import { ConfirmationDialogComponent } from './dialog/confirmation-dialog/confirmation-dialog.component';
import { InformationDialogComponent } from './dialog/information-dialog/information-dialog.component';
import { WarningDailogComponent } from './dialog/warning-dailog/warning-dailog.component';

@NgModule({
  declarations: [InstitutionInmatesComponent,
    InmatesHomeComponent, CciHomeComponent,
    InstitutionInmatesViewComponent,
    ConfirmationDialogComponent,
    InformationDialogComponent,
    WarningDailogComponent],
  imports: [
    CommonModule,
    DataEntryRoutingModule,
    FormsModule,
    MatDialogModule,
    ReactiveFormsModule,
    FormModule,
    TableModule,
    MatCardModule,
    MatTabsModule,
    MatButtonModule,
    MatTooltipModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatSlideToggleModule
  ],
  entryComponents: [ConfirmationDialogComponent, InformationDialogComponent,WarningDailogComponent],
  providers: [
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: true } }
  ]
})
export class DataEntryModule { }

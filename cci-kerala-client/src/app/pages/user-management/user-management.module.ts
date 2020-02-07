import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UsersComponent } from './users/users.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule, MatCardModule, MatTabsModule,
   MatButtonModule, MatTooltipModule, MatToolbarModule, 
   MatIconModule, MatSidenavModule, MatListModule, MatSlideToggleModule, MatExpansionModule, MatFormFieldModule, MatFormFieldControl, MatInputModule, MatSelectModule, MAT_DIALOG_DEFAULT_OPTIONS, ErrorStateMatcher, ShowOnDirtyErrorStateMatcher } from '@angular/material';
import { TableModule } from 'lib-table/public_api';
import { ChangePasswordComponent } from './dailog/change-password/change-password.component';
import { ConfirmationDailogComponent } from './dailog/confirmation-dailog/confirmation-dailog.component';

@NgModule({
  declarations: [UsersComponent, ChangePasswordComponent, ConfirmationDailogComponent],
  imports: [
    CommonModule,
    UserManagementRoutingModule,
    FormsModule,
    MatDialogModule,
    ReactiveFormsModule,
    TableModule,
    MatCardModule,
    MatTabsModule,
    MatButtonModule,
    MatTooltipModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatSlideToggleModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ], entryComponents: [ChangePasswordComponent,ConfirmationDailogComponent],
  providers:[{ provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: true }}]
})
export class UserManagementModule { }

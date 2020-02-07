import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ViewDetailsComponent } from './view-details/view-details.component';
import { InmatesManagementComponent } from './inmates-management/inmates-management.component';
import { InstitutionManagementComponent } from './institution-management/institution-management.component';

const routes: Routes = [
  {path: 'inmates-management', pathMatch: 'full', component: InmatesManagementComponent, data: { title: 'Inmates Management'}},
  {path: 'institution-management', pathMatch: 'full', component: InstitutionManagementComponent,data: { title: 'Institution Management'}},
  {path: 'view-details/:id', pathMatch: 'full', component: ViewDetailsComponent,data: { title: 'View Data'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SubmissionRoutingModule { }

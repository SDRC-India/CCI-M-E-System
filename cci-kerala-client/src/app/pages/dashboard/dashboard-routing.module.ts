import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardViewComponent } from './dashboard-view/dashboard-view.component';
import { MapDetailsComponent } from './map-details/map-details.component';
import { CcilistViewComponent } from './ccilist-view/ccilist-view.component';
import { InmatesViewComponent } from './inmates-view/inmates-view.component';

const routes: Routes = [
  {path: '',  pathMatch: 'full', component: DashboardViewComponent,data:{title:'CCMIS-Dashboard'}},
  { path: 'map-details/:id', pathMatch: 'full', component: MapDetailsComponent, data: { title: 'CCMIS-Dashboard' }},
  { path: 'ccilist-view/:id', pathMatch: 'full', component: CcilistViewComponent, data: { title: 'CCMIS-Submission Details' }},
  { path: 'inmates-view/:id', pathMatch: 'full', component: InmatesViewComponent, data: { title: 'CCMIS-Inmate Dashboard' }}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }

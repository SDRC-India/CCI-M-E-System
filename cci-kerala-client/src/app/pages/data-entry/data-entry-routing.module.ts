import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InstitutionInmatesComponent } from './institution-inmates/institution-inmates.component';
import { InmatesHomeComponent } from './inmates-home/inmates-home.component';
import { CciHomeComponent } from './cci-home/cci-home.component';
import { InstitutionInmatesViewComponent } from './institution-inmates-view/institution-inmates-view.component';

const routes: Routes = [
  // {path: '', pathMatch: 'full', component: InmatesHomeComponent, data: {title: 'Inmates Home'}},
  // {path: 'cci', pathMatch: 'full', component: CciHomeComponent, data: {title: 'CCI Home'}},
  {path: '', pathMatch: 'full', component: CciHomeComponent, data: {title: 'CCI Home'}},
  {path: 'inmates', pathMatch: 'full', component: InmatesHomeComponent, data: {title: 'Inmates Home'}},
  {path: 'dataEntry/:id', pathMatch: 'full', component: InstitutionInmatesComponent, data: {title: 'CCMIS-DataEntry'}},
  {path: 'dataView/:id', pathMatch: 'full', component: InstitutionInmatesViewComponent, data: {title: 'CCMIS-View'}}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DataEntryRoutingModule { }

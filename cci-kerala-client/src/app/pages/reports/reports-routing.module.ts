import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InmatesRawdataComponent } from './inmates-rawdata/inmates-rawdata.component';
import { SummaryReportComponent } from './summary-report/summary-report.component';

const routes: Routes = [
  {path: 'inmates-rawdata', pathMatch: 'full', component: InmatesRawdataComponent,data:{title:'Inmates Raw Data'}},
  {path: 'summary-report', pathMatch: 'full', component: SummaryReportComponent,data:{title:'Summary Report'}}

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportsRoutingModule { }

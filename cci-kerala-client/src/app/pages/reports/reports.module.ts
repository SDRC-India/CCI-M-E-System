import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { InmatesRawdataComponent } from './inmates-rawdata/inmates-rawdata.component';
import { MatCardModule, MatDialogModule, MatButtonModule, MatSelectModule, MatTooltipModule } from '@angular/material';
import { FormModule } from 'lib/public_api';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { SummaryReportComponent } from './summary-report/summary-report.component';
import { TableModule } from 'lib-table/public_api';

@NgModule({
  declarations: [InmatesRawdataComponent, SummaryReportComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatDialogModule,
    MatButtonModule,
    FormModule,
    ReactiveFormsModule,
    FormsModule,
    MatSelectModule,
    ReportsRoutingModule,
    TableModule,
    MatTooltipModule
  ]
})
export class ReportsModule { }

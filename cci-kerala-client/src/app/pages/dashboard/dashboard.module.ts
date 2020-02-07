import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgmCoreModule } from '@agm/core';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardViewComponent } from './dashboard-view/dashboard-view.component';

import { MatCardModule, MatTooltipModule, MatButtonModule, MatSelectModule, MatIconModule } from '@angular/material';
import { TableModule } from 'lib-table/public_api';
import { MapDetailsComponent } from './map-details/map-details.component';
import { CcilistViewComponent } from './ccilist-view/ccilist-view.component';
import { InmatesViewComponent } from './inmates-view/inmates-view.component';
import { FormsModule } from '@angular/forms';
import { FormModule } from 'lib/public_api';
import { BarChartComponent } from './bar-chart/bar-chart.component';
import { PieChartComponent } from './pie-chart/pie-chart.component';

@NgModule({
  declarations: [DashboardViewComponent, MapDetailsComponent, CcilistViewComponent, InmatesViewComponent, BarChartComponent, PieChartComponent],
  imports: [
    CommonModule,
    MatCardModule,
    TableModule,
    MatTooltipModule,
    MatButtonModule,
    DashboardRoutingModule,
    MatSelectModule,
    FormsModule,
    MatIconModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDCAt2fOQ7y1wujCYU8oDe31S4mGj4jMz4'
    }),
    FormModule
  ]
})
export class DashboardModule { }

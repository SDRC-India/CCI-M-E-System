import { Component, OnInit } from '@angular/core';
import { DashboardServiceService } from '../dashboard-service.service';
import { Router } from '@angular/router';
import { Constants } from 'src/app/constants';
import { Location } from '@angular/common';

@Component({
  selector: 'scps-inmates-view',
  templateUrl: './inmates-view.component.html',
  styleUrls: ['./inmates-view.component.scss']
})
export class InmatesViewComponent implements OnInit {

  tableData:any;
  tableColumns: string[];
  dashboardLandingPages:any;
  CCIDashboardData:any;
  subSectorsData: any;
  quickValue: any;
  typeOfDatas:string[]=[]

  constructor(public dashboardservice: DashboardServiceService, private router: Router, private location: Location) { }

  ngOnInit() {

    this.dashboardservice.getDashboardInmateData().subscribe((data) => {
      this.tableData = data.tableData;
      this.tableColumns = data.tableColumn;
      this.dashboardLandingPages = data.dashboardLandingPages;
      this.typeOfDatas=Object.keys(this.dashboardLandingPages);
    });
  }

  actionClicked(rowObj: any) {
    this.dashboardservice.submissionId = rowObj.rowObj.id;
    if (rowObj.target.includes('approved-view')) {
      this.router.navigate(['/dashboard/ccilist-view', Constants.FORM_ID_INMATE ]);
    }
  }
  goback() {
    this.location.back();
  }
  removeReference(data){
    return JSON.parse(JSON.stringify(data));
  }

  getKeys(data) {
    return Object.keys(data);
  }

}

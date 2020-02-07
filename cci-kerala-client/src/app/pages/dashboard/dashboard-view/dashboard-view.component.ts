import { Component, OnInit } from '@angular/core';
import { DashboardServiceService } from '../dashboard-service.service';
import { Router } from '@angular/router';
import { Constants } from 'src/app/constants';
declare var $: any;
@Component({
  selector: 'scps-dashboard-view',
  templateUrl: './dashboard-view.component.html',
  styleUrls: ['./dashboard-view.component.scss']
})
export class DashboardViewComponent implements OnInit {

  tableData: any;
  tableColumns: string[];
  dashboardLandingPages: any;
  objQuickStarts: any = {};
  zoom: number = Constants.DEFAULT_ZOOM;
  area: string;
  districts: any[] = []
  selectedDistrict: any;
  totalData: any;
  pieData:any;
  mapData: { lat: number, lng: number, label: String, draggable: boolean, name: string } = { lat: 0, lng: 0, label: '', draggable: false, name: '' };
  lat: number;
  lng: number;
  label: string;
  quickValue: any;
  districtData: any;
  districtList:any;
  subSectorsData: any;
  typeOfDatas:string[]=[];
  checkboxSelected:boolean;
  constructor(private dashboardservice: DashboardServiceService, private router: Router) { 
  }

  ngOnInit() {
    this.dashboardservice.getDashboardTableData(0,0).subscribe((data) => {
      this.totalData = data.tableData;
      this.tableData = data.tableData;
      this.tableColumns = data.tableColumn;
      this.dashboardLandingPages = data.dashboardLandingPages;
      this.districtList = data.districts;
      this.typeOfDatas=Object.keys(this.dashboardLandingPages);
      if(this.districtList.length)
      this.selectedDistrict=this.districtList[0];
    });
    this.area = JSON.parse(localStorage.getItem(Constants.USER_DETAILS)).sessionMap.area.areaName
  }
  actionClicked(rowObj: any) {
    this.dashboardservice.submissionId = rowObj.rowObj.id;
    if (rowObj.target.includes('approved-view')) {
      this.router.navigate(['/dashboard/ccilist-view', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('approved-map')) {
      this.mapData.lat = parseFloat(rowObj.rowObj.lat);
      this.mapData.lng = parseFloat(rowObj.rowObj.lng);
      this.mapData.draggable = rowObj.rowObj.draggable;
      this.mapData.name = rowObj.rowObj['Name of CCI'][0].value;
      $('#myModal').modal('show');
    }
    else if (rowObj.target.includes('approved-child')) {
      this.dashboardservice.selectedCCI = rowObj.rowObj.cciid;
      this.dashboardservice.selectedCCIName = rowObj.rowObj['Name of CCI'][0].value;
      this.dashboardservice.sqftPerChild = rowObj.rowObj['Average Sq. ft. area per child'];
      this.router.navigate(['/dashboard/inmates-view', Constants.FORM_ID_INMATE]);
    }
  }

  districtSelected(district) {
    if (district) {
      this.tableData = this.totalData.filter(d => d.districtId == district.areaId);
    }
    else {
      this.tableData = this.totalData;
    }
  }

  filterExceedingCol(value:boolean)
  {
    this.checkboxSelected=value;
    if(value)
    {
      this.tableData = this.totalData.filter(d => (d['Maximum Strength Exceeded'] as string).toLowerCase()== 'yes');
    }
    else {
      this.tableData = this.totalData;
    }
  }
  removeReference(data){
    return JSON.parse(JSON.stringify(data));
  }

  districtSelection(){
    if(this.selectedDistrict.areaId==0)
    {
      this.dashboardservice.getDashboardTableData(0,0).subscribe((data) => {
        this.totalData = data.tableData;
        this.tableData = data.tableData;
        this.tableColumns = data.tableColumn;
        this.dashboardLandingPages = data.dashboardLandingPages;
        this.typeOfDatas=Object.keys(this.dashboardLandingPages);
        this.checkboxSelected 
        if (this.checkboxSelected ) {
          this.tableData = this.totalData.filter(d => (d['Maximum Strength Exceeded'] as string).toLowerCase() == 'yes');
        }
      });
      this.area = JSON.parse(localStorage.getItem(Constants.USER_DETAILS)).sessionMap.area.areaName
    }
    else
    {
    this.dashboardservice.getDashboardTableData(this.selectedDistrict.areaId,this.selectedDistrict.areaLevel.areaLevelId).subscribe((data) => {
      this.totalData = data.tableData;
      this.tableData = data.tableData;
      this.tableColumns = data.tableColumn;
      this.dashboardLandingPages = data.dashboardLandingPages;
      this.typeOfDatas=Object.keys(this.dashboardLandingPages);
      this.checkboxSelected
      if (this.checkboxSelected) {
        this.tableData = this.totalData.filter(d => (d['Maximum Strength Exceeded'] as string).toLowerCase() == 'yes');
      }
    });
    this.area=this.selectedDistrict.areaName
    }
  }

  getKeys(data)
  {
    return Object.keys(data);
  }
}

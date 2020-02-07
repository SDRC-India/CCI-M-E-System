import { Component, OnInit } from '@angular/core';
import { ReportsService } from '../reports.service';
import saveAs from 'save-as';
import { Constants } from 'src/app/constants';
import { ToastrService } from 'ngx-toastr';
declare var $: any;

@Component({
  selector: 'scps-inmates-rawdata',
  templateUrl: './inmates-rawdata.component.html',
  styleUrls: ['./inmates-rawdata.component.scss']
})
export class InmatesRawdataComponent implements OnInit {
  errorMessage: any;
  districtList: any;
  districtData: any;
  constructor(private reportService: ReportsService,private toaster:ToastrService) { }

  ngOnInit() {
    this.reportService.getDistrict().subscribe((data) => {
        this.districtList = data;
    });
  }
  download() {
    this.reportService.downloadInmatesRawData(this.districtData.areaId).subscribe(
      data => {
        saveAs(data, this.districtData.areaName + Date.now() + '.xlsx');
      },
      error => {
         this.errorMessage = Constants.SERVER_ERROR_MESSAGE;
         this.toaster.error(this.errorMessage)
      }
     );
  }
}

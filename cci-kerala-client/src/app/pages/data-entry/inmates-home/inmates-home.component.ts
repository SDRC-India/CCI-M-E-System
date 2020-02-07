import { Component, OnInit } from '@angular/core';
import { CommonService } from '../service/common.service';
import { Router } from '@angular/router';
import { Constants } from 'src/app/constants';
import * as status from 'http-status-codes';
import { ToastrService } from 'ngx-toastr';
import { ConfirmationDialogComponent } from '../dialog/confirmation-dialog/confirmation-dialog.component';
import { MatDialog } from '@angular/material';
import { InformationDialogComponent } from '../dialog/information-dialog/information-dialog.component';
import { WarningDailogComponent } from '../dialog/warning-dailog/warning-dailog.component';

@Component({
  selector: 'scps-inmates-home',
  templateUrl: './inmates-home.component.html',
  styleUrls: ['./inmates-home.component.scss']
})
export class InmatesHomeComponent implements OnInit {
  tableData: any;
  tableColumns: string[];
  cciInfo: any = [];
  cciKeys: any;

  constructor(private commonService: CommonService,
              private router: Router, private toastr: ToastrService, public dialog: MatDialog) { }

  ngOnInit() {
    this.commonService.getLandingData().subscribe((data) => {
      this.cciInfo = data.cciInformation;
      this.cciKeys = Object.keys(this.cciInfo);
      this.cciKeys.splice(-1, 1);
      this.tableData = data.inmatesDetailsMap.tableData;
      this.tableColumns = data.inmatesDetailsMap.tableColumn;
    });
  }
  clickToNavigate(id: number) {

    if (this.cciInfo['Current Strength'] >= this.cciInfo['Maximum Strength'])
    {
      this.dialog.open(WarningDailogComponent, { width: '400px', data: { msg: Constants.EXCEEDING_SANCTIONED_LIMIT } }).afterClosed().subscribe(d=>{
        if (d)
        {
        this.commonService.submissionId = 0;
      this.router.navigate(['/ccihome/dataEntry', id]);
        }
    });
    }

    else{
    this.commonService.submissionId = 0;
    this.router.navigate(['/ccihome/dataEntry', id]);
    }
  }
  actionClicked(rowObj: any) {
    this.commonService.submissionId = rowObj.rowObj.id;
    this.commonService.submissionId = rowObj.rowObj.id;
    if (rowObj.target.includes('approved-edit')) {
      this.validateForEdit(rowObj).then((val) => {
        if (val) {
          this.router.navigate(['/ccihome/dataEntry', Constants.FORM_ID_INMATE]);
        }
      });
    } else if (rowObj.target.includes('approved-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INMATE]);
    } else if (rowObj.target.includes('draft-edit')) {
      this.router.navigate(['/ccihome/dataEntry', Constants.FORM_ID_INMATE]);
    } else if (rowObj.target.includes('pending-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INMATE]);
    } else if (rowObj.target.includes('draft-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INMATE]);
    } else if (rowObj.target.includes('rejected-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INMATE]);
    } else if (rowObj.target.includes('rejected-edit')) {
      this.router.navigate(['/ccihome/dataEntry', Constants.FORM_ID_INMATE]);
    } else if (rowObj.target.includes('draft-delete') || rowObj.target.includes('rejected-delete')) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent,
        { width: '400px', data: { msg: 'Do you want to delete the record?' } });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.commonService.deleteSubmissionData(rowObj.rowObj.id, Constants.FORM_ID_INMATE).subscribe(
            (data) => {
              if (data.statusCode === status.OK) {
                this.toastr.success(data.message, 'Success', {
            
                });
                this.commonService.getLandingData().subscribe((dataVal) => {
                  this.cciInfo = dataVal.cciInformation;
                  this.cciKeys = Object.keys(this.cciInfo);
                  this.cciKeys.splice(-1, 1);
                  this.tableData = dataVal.inmatesDetailsMap.tableData;
                  this.tableColumns = dataVal.inmatesDetailsMap.tableColumn;
                });
              } else if (data.statusCode === status.NOT_MODIFIED) {
                this.toastr.warning(data.message, 'Warning', {
            
                });
              }
            });
        }
      });
    }
  }
  validateForEdit(rowObj) {
    return new Promise((resolve, reject) => {
      let newObj: [] = this.tableData.filter(it => it.name === 'DRAFT')[0].data.filter(item => item['Inmate Id'] === rowObj.rowObj['Inmate Id']);
      if (newObj.length > 0) {
        this.dialog.open(InformationDialogComponent, { width: '400px', data: { msg: 'Already this record in draft list.' } });
        resolve(false);
      }
      newObj = this.tableData.filter(it => it.name === 'PENDING')[0].data.filter(item => item['Inmate Id'] === rowObj.rowObj['Inmate Id']);
      if (newObj.length > 0) {
        this.dialog.open(InformationDialogComponent, { width: '400px', data: { msg: 'Already this record in pending list.' } });
        resolve(false);
      }
      newObj = this.tableData.filter(it => it.name === 'REJECTED')[0].data.filter(item => item['Inmate Id'] === rowObj.rowObj['Inmate Id']);
      if (newObj.length > 0) {
        this.dialog.open(InformationDialogComponent, { width: '400px', data: { msg: 'Already this record in Rejected list.' } });
        resolve(false);
      }
      resolve(true)
    });
  }
}

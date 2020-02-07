import { Component, OnInit } from '@angular/core';
import { CommonService } from '../service/common.service';
import { Router } from '@angular/router';
import { Constants } from 'src/app/constants';
import * as status from 'http-status-codes';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material';
import { ConfirmationDialogComponent } from '../dialog/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'scps-cci-home',
  templateUrl: './cci-home.component.html',
  styleUrls: ['./cci-home.component.scss']
})
export class CciHomeComponent implements OnInit {
  tableData: any;
  tableColumns: string[];
  cciInfo: any = [];
  cciKeys: any;

  constructor(private commonService: CommonService, private router: Router,
              private toastr: ToastrService, public dialog: MatDialog) { }

  ngOnInit() {
    this.commonService.getInstitutionLandingData().subscribe((data) => {
      this.cciInfo = data.cciInformation;
      this.cciKeys = Object.keys(this.cciInfo);
      // this.cciKeys.splice(-1, 1);
      this.tableData = data.tableData;
      this.tableColumns = data.tableColumn;
    });
  }
  clickToNavigate(id: number) {
    this.commonService.submissionId = 0;
    this.router.navigate(['/ccihome/dataEntry', id]);
  }
  actionClicked(rowObj: any) {
    this.commonService.submissionId = rowObj.rowObj.id;
    if (rowObj.target.includes('approved-edit')) {
      this.router.navigate(['/ccihome/dataEntry', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('approved-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('draft-edit')) {
      this.router.navigate(['/ccihome/dataEntry', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('pending-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('draft-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('rejected-view')) {
      this.router.navigate(['/ccihome/dataView', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('rejected-edit')) {
      this.router.navigate(['/ccihome/dataEntry', Constants.FORM_ID_INSTITUTION]);
    } else if (rowObj.target.includes('draft-delete') || rowObj.target.includes('rejected-delete')) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent,
        { width: '400px', data: { msg: 'Do you want to delete the record?' } });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.commonService.deleteSubmissionData(rowObj.rowObj.id, Constants.FORM_ID_INSTITUTION).subscribe(
            (data) => {
              if (data.statusCode === status.OK) {
                this.toastr.success(data.message, 'Success', {
                });
                this.commonService.getInstitutionLandingData().subscribe((dataVal) => {
                  this.cciInfo = dataVal.cciInformation;
                  this.cciKeys = Object.keys(this.cciInfo);
                  // this.cciKeys.splice(-1, 1);
                  this.tableData = dataVal.tableData;
                  this.tableColumns = dataVal.tableColumn;
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
}

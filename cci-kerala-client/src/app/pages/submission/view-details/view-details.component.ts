import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SubmissionService } from '../service/submission.service';
import { MatDialog } from '@angular/material';
import { RejectionReasonComponent } from '../dialog/rejection-reason/rejection-reason.component';
import { ApprovalRejectionModel } from '../models/approval-rejection-model';
import * as status from 'http-status-codes';
import { Constants } from 'src/app/constants';
import { Location } from '@angular/common';

@Component({
  selector: 'scps-view-details',
  templateUrl: './view-details.component.html',
  styleUrls: ['./view-details.component.scss']
})
export class ViewDetailsComponent implements OnInit {

  formDetails: any[] = [];
  formSections: any = [];
  sectionName: any;
  selectedSection: any;
  rejectedSection: number[] = [];
  formId: any = 0;
  rejectedButtonDisable = true;
  constructor(private submissionService: SubmissionService, private activeRoute: ActivatedRoute,
    private toastr: ToastrService, public dialog: MatDialog, private router: Router,
    private _location: Location) { }

  ngOnInit() {
    this.activeRoute.params.subscribe(d => {
      this.getQuestionList(d.id);
    }
    );
    if (this.submissionService.submissionId == 0) {
      this._location.back();
    }
  }
  getQuestionList(formId) {
    this.formId = formId;
    this.submissionService.getQuestionListForView(formId).subscribe((data) => {
      this.formDetails = data;
      this.formDetails = this.formDetails.sort((n1, n2) => {
        if (n1.sectionOrder > n2.sectionOrder) {
          return 1;
        }
        if (n1.sectionOrder < n2.sectionOrder) {
          return -1;
        }
        return 0;
      });
      this.defaultViewforSection();
    });
  }
  defaultViewforSection() {
    // Default view of section
    this.selectedSection = this.formDetails[0];
    this.sectionName = this.formDetails[0].name;
    this.formSections = this.formDetails[0].questions;
    this.formSections = this.formSections.sort((n1, n2) => {
      if (n1.key > n2.key) {
        return 1;
      }
      if (n1.key < n2.key) {
        return -1;
      }
      return 0;
    });
  }
  checkValue(event) {
    const totalRejected = this.formDetails.filter(obj => obj.rejected === true);
    if (totalRejected.length > 0) {
      this.rejectedButtonDisable = false;
    } else {
      this.rejectedButtonDisable = true;
    }
  }
  approvedClicked() {
    const submissionData = this.makeNewFormList(this.formDetails);

    let approvalRejectionModel: ApprovalRejectionModel = {
      approved: true,
      questionModels: submissionData,
      rejectedSections: [],
      remarks: '',
      formId: parseInt(this.formId)
    }

    this.submissionService.approveRejectSubmission(approvalRejectionModel).subscribe(
      (data) => {
        if (data.statusCode === status.OK) {
          this.toastr.success(data.message, 'Success', {

          });

        } else {
          this.toastr.error(data.message, 'Error', {

          });
        }


        if (this.formId === Constants.FORM_ID_INSTITUTION) {
          this._location.back();
        } else {
          this._location.back();
        }

      }, (error) => {
        this.toastr.error('Cannot connect to server', 'Error', {

        });
      });
  }
  goback() {
    this._location.back();
  }
  rejectClicked() {
    const submissionData = this.makeNewFormList(this.formDetails);

    const dialogRef = this.dialog.open(RejectionReasonComponent);
    this.submissionService.rejectionReason = '';
    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        let approvalRejectionModel: ApprovalRejectionModel = {
          approved: false,
          questionModels: submissionData,
          rejectedSections: this.rejectedSection,
          remarks: this.submissionService.rejectionReason,
          formId: parseInt(this.formId)
        }
        this.submissionService.approveRejectSubmission(approvalRejectionModel).subscribe(
          (data) => {
            if (data.statusCode === status.OK) {
              this.toastr.success(data.message, 'Success', {
              });
            } else {
              this.toastr.error(data.message, 'Error', {
              });
            }

            if (this.formId === Constants.FORM_ID_INSTITUTION) {
              this._location.back();
            } else {
              this._location.back();
            }

          }, (error) => {
            this.toastr.error('Cannot connect to server', 'Error', {
            });
          }
        );
      }
    });
  }


  makeNewFormList(formlist) {
    const newformlist = [];
    this.rejectedSection = [];
    for (const item of formlist) {
      if (item.rejected) {
        this.rejectedSection.push(item.id);
      }
      // tslint:disable-next-line:prefer-for-of
      for (let index = 0; index < item.questions.length; index++) {
        newformlist[newformlist.length] = Object.assign(item.questions[index]);
      }
    }
    return newformlist;
  }

}

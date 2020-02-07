import { Component, OnInit, Inject } from '@angular/core';
import { SubmissionService } from 'src/app/pages/submission/service/submission.service';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'scps-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {

  reasons: any;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private submissionService: SubmissionService) {}
  submitClicked() {
   this.submissionService.rejectionReason = this.reasons;
  }
}

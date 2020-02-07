import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { SubmissionService } from '../../service/submission.service';

@Component({
  selector: 'scps-rejection-reason',
  templateUrl: './rejection-reason.component.html',
  styleUrls: ['./rejection-reason.component.scss']
})
export class RejectionReasonComponent {
  reasons: any;
  constructor(private submissionService: SubmissionService) {}
  submitClicked() {
   this.submissionService.rejectionReason = this.reasons
  }
}

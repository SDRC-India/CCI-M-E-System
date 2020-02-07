import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/constants';
import { map, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ApprovalRejectionModel } from '../models/approval-rejection-model';

@Injectable({
  providedIn: 'root'
})
export class SubmissionService {
  formId;
  submissionId = 0;
  rejectionReason='';
  constructor(private httpClient: HttpClient) { }
  getInmatesManamentData(id: any): any {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getApprovalPendingList?id=' + id).pipe(
     map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
  getQuestionListForView(formId: any): any {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getViewDataForApproval?formId=' + formId + '&submissioId=' +
     this.submissionId).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }

  approveRejectSubmission(approvalRejectionModel: ApprovalRejectionModel): any {
    return this.httpClient.post(Constants.API_GATE_WAY + 'approveRejectSubmission',approvalRejectionModel).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
}

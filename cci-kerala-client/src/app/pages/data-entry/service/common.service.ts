import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { filter, map, catchError, } from 'rxjs/operators';
import { Constants } from 'src/app/constants';
import { IQuestion } from 'lib/public_api';

@Injectable({
  providedIn: 'root'
})
export class CommonService {
  finalizeForm(submissionData: IQuestion[]): any {
    return this.httpClient.post(Constants.API_GATE_WAY + 'finalizeInstitutionInmates', submissionData ).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
  formId;
  submissionId = 0;
  constructor(private httpClient: HttpClient) {
  }
  public selectedForm;
  getQuestionList(formId): Observable<any> {
    this.formId = formId;
    return this.httpClient.get(Constants.API_GATE_WAY + 'getQuestion?formId=' + formId + '&submissioId=' + this.submissionId).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );

  }
  getChildDetailsList(): any {
    return this.httpClient.get('assets/inmate_data.json').pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
  uploadFile(file,columnName): Observable<HttpEvent<{}>> {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    formdata.append('formId', this.formId);
    formdata.append('columnName', columnName);

    const req = new HttpRequest('POST', Constants.API_GATE_WAY + 'uploadFile', formdata, {
      reportProgress: true    });

    return this.httpClient.request(req);
  }


  getLandingData(): any {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getLandingData').pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }


  getInstitutionLandingData(): any {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getInstitutionLandingData').pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
  getQuestionListForView(formId: any): any {
    this.formId = formId;
    return this.httpClient.get(Constants.API_GATE_WAY + 'getViewData?formId=' + formId + '&submissioId=' + this.submissionId).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
  saveFormData(formData: IQuestion[]): any {
    return this.httpClient.post(Constants.API_GATE_WAY + 'draftInstitutionInmates', formData ).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }

  deleteSubmissionData(submissioId:number,formId:number):any
  {
    return this.httpClient.get(Constants.API_GATE_WAY + 'deleteSubmission?formId=' + formId + '&submissionId=' + submissioId).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
}

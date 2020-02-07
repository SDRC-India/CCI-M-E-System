import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/constants';
import { map, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Data } from './model/data';
@Injectable({
  providedIn: 'root'
})
export class DashboardServiceService {

  formId;
  submissionId = 0;
  selectedCCI = 0;
  selectedCCIName='';
  sqftPerChild='0';
  constructor(private httpClient: HttpClient) { }
  getDashboardTableData(areaId,areaLevelId): any {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getDashboardData?areaId='+areaId+'&areaLevelId='+areaLevelId)
  }

  getDashboardInmateData() :any
  {
    return this.httpClient.get(Constants.API_GATE_WAY +
       'getDashboardInmateData?cciId='+this.selectedCCI+'&sqft='+this.sqftPerChild)
  }

  getQuestionListForView(formId: any): any {
    this.formId = formId;
    return this.httpClient.get(Constants.API_GATE_WAY + 'getViewData?formId=' + formId + '&submissioId=' + this.submissionId).pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }
  getInmatesManamentData(): any {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getApprovalPendingList').pipe(
     map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }

  getMapData(){
    return this.httpClient.get<Data[]>('assets/mapdata.json');
  }

  getcciDashboardData(){
    return this.httpClient.get('assets/ccidashboard.json');
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from 'src/app/constants';
import { throwError, Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  constructor(private httpClient: HttpClient) { }

  downloadInmatesRawData(districtId): any {
    return this.httpClient.post(Constants.API_GATE_WAY + 'downloadInmatesRawData?districtId='+districtId, '', {
      responseType: 'blob'
    });
  }

  getDistrict():Observable<any> {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getDistrict').pipe(
      map((res: Response) => res),
      catchError((res: Response) => throwError(res))
    );
  }

  getSummaryReport(areaId,areaLevelId):Observable<any> {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getSummaryReportTable?areaId='+areaId+"&areaLevelId="+areaLevelId);
  }
}

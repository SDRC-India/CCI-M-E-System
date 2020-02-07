import { Injectable } from '@angular/core';
import { map, catchError } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';
import { Constants } from 'src/app/constants';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  formId;
  submissionId = 0;
  constructor(private httpClient: HttpClient) {
  }
  public selectedForm;
  getAreaLevel() {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getAllSelectionData');
  }
  getAllUsers(): Observable<any> {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getAllUsers');
  }

  enableUser(userId): Observable<any> {
    return this.httpClient.get(Constants.API_GATE_WAY + 'enableUser?userId=' + userId);
  }

  disableUser(userId): Observable<any> {
    return this.httpClient.get(Constants.API_GATE_WAY + 'disableUser?userId=' + userId);
  }
  submitUser(formData): Observable<any>   {
    return this.httpClient.post(Constants.API_GATE_WAY + 'createUserSubmit', formData);
  }
  submitCCI(formData): Observable<any>  {
    return this.httpClient.post(Constants.API_GATE_WAY + 'createCCI', formData);
  }
  changePassword(formData): Observable<any>  {
    return this.httpClient.post(Constants.API_GATE_WAY + 'resetPassword', formData);
  }
}

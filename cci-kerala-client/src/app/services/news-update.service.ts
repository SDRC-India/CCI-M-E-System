import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Constants } from '../constants';
import { Observable, Subject } from 'rxjs';
declare var $: any;
@Injectable({
  providedIn: 'root'
})
export class NewsUpdateService {
  newsUpdatesId: number;
  newsTitle: string;
  newsLinks: string;
  newsDescription: string;
  createdDate: string;
  createdBy: string;
  isLive: Boolean;
  isEditing: Boolean;
  isDeleting: Boolean;
  constructor(private httpClient: HttpClient) { }
  private subject = new Subject<any>();
  clearErrorMsg(id) {
    $('#' + id).html('');
  }

  getAllNewsUpdates() {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getNewsUpdate');
  }

  saveNewsUpdates(newsObject): Observable<any> {
    return this.httpClient.post(Constants.API_GATE_WAY + 'saveNews', newsObject);
  }

  getNewsTable() {
    return this.httpClient.get(Constants.API_GATE_WAY + 'getNewsTable');
  }

  deleteNews(id): Observable<any> {
    return this.httpClient.get(Constants.API_GATE_WAY + 'deleteNews?newsId=' + id);
  }
  sendMessage(message: string) {
    this.subject.next({ text: message });
  }

  clearMessages() {
    this.subject.next();
  }

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }
}

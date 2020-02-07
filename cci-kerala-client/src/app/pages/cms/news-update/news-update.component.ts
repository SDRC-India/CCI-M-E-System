import { Component, OnInit } from '@angular/core';
import { NewsUpdateService } from '../../../services/news-update.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import * as status from 'http-status-codes';
import { Constants } from 'src/app/constants';
import { FormGroup, FormBuilder, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { MatDialog } from '@angular/material';
declare var $: any;
@Component({
  selector: 'scps-news-update',
  templateUrl: './news-update.component.html',
  styleUrls: ['./news-update.component.scss']
})
export class NewsUpdateComponent implements OnInit {
  buttonName: string;
  selectedObj: any;
  itemsPerPage = 5;
  searchText: string;
  tableData = [];
  tableColumns = [];
  newsUpdateForm: FormGroup;
  isExpanded: boolean;
  constructor(private fb: FormBuilder, private newsUpdateService: NewsUpdateService,
              private spinner: NgxSpinnerService, private toastr: ToastrService, public dialog: MatDialog) {
    this.newsUpdateForm = this.fb.group({
      newsTitle: new FormControl('', [Validators.required, Validators.maxLength(250)]),
      // tslint:disable-next-line:max-line-length
      newsUrl: new FormControl('', [Validators.required, Validators.pattern(/^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-zA-Z0-9]+([\-\.]{1}[a-zA-Z0-9]+)*\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(\/.*)?$/), Validators.maxLength(250)])
    });
  }

  ngOnInit() {
    this.getNewsTable();
    this.buttonName = 'Submit';
  }
  getNewsTable() {
    this.newsUpdateService.getNewsTable().subscribe(res => {
      this.tableData = res['tableData'];
      this.tableColumns = res['tableHeader'];
    }, err => {
      console.log(err);
    });
  }
  reset() {
    this.buttonName = 'Submit';
    this.selectedObj = null;
    this.isExpanded = false;
    this.newsUpdateForm.reset();
  }
  submit(formDirective: FormGroupDirective) {
    if (this.newsUpdateForm.valid) {
      const newsObj = {
        newsUpdateId: this.selectedObj == null ? 0 : this.selectedObj.id,
        newsTitle: this.newsUpdateForm.controls['newsTitle'].value,
        newsUrl: this.newsUpdateForm.controls['newsUrl'].value,
      }
      this.newsUpdateService.saveNewsUpdates(newsObj).subscribe(res => {
        if (res.statusCode === status.OK) {
          this.toastr.success(res.message);
          this.newsUpdateService.sendMessage('Update News & Updates');
          this.getNewsTable();
          formDirective.resetForm();
          this.reset();
        } else {
          this.toastr.error(res.message);
        }
      }, err => {
        this.toastr.error(Constants.SERVER_ERROR_MESSAGE);
      });
    }
  }
  appendHttp(link: string) {
    if (!link.startsWith('http')) {
      return 'http://' + link;
    } else {
      return link;
    }
  }
  editNewsDetails(newsModel) {
    document.getElementById('newstitle').focus();
    $('html, body').animate({
      scrollTop: 0
    }, 1000);
    this.isExpanded = true;
    this.buttonName = "Update";

    this.selectedObj = newsModel;
    this.newsUpdateForm.controls['newsTitle'].setValue(newsModel.Title);
    this.newsUpdateForm.controls['newsUrl'].setValue(newsModel.URL);
  }


  actionClicked(rowObj) {
    const news = rowObj.rowObj;
    if (rowObj.target.includes('edit')) {
      this.editNewsDetails(news);
    } else if (rowObj.target.includes('delete')) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent,
        { width: '400px', data: { msg: 'Do you want to delete the record?' } });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.newsUpdateService.deleteNews(news.id).subscribe(
            data => {
              if (data.statusCode === status.OK) {
                this.toastr.success(data.message, 'Success');
                this.newsUpdateService.sendMessage('Update News & Updates');
                this.getNewsTable();
              } else if (data.statusCode === status.BAD_REQUEST) {
                this.toastr.warning(data.message, 'Warning');
              }
            }
          );
        }
      });
    }
  }
}

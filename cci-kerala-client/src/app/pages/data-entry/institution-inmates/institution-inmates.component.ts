import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonService } from '../service/common.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import * as status from 'http-status-codes';
import { MatSidenav, MatDialog } from '@angular/material';
import { FormComponent } from 'lib/public_api';
import { Location } from '@angular/common';
import { InformationDialogComponent } from '../dialog/information-dialog/information-dialog.component';
import { Constants } from 'src/app/constants';
declare var $: any;
@Component({
  selector: 'scps-institution-inmates',
  templateUrl: './institution-inmates.component.html',
  styleUrls: ['./institution-inmates.component.scss'],
  host: {
    '(window:resize)': 'onResize($event)',
    '(window:popstate)': 'onPopState($event)',
    '(window:beforeunload)': 'beforeunload($event)'
  }
})
export class InstitutionInmatesComponent implements OnInit {
  formDetails: any[] = [];
  formSections: any = [];
  sectionName: any;
  formId = 2;
  selectedSection: any;
  finalize = false;
  navShow: boolean = true;
  valid = false;
  mode: string = 'push';
  navOpened: boolean = false;
  disablePush: boolean = false;
  @ViewChild('sdrcForm') sdrcForm: FormComponent;
  @ViewChild('sidenav') sideNav: MatSidenav;
  constructor(private commonService: CommonService,
    private activeRoute: ActivatedRoute,
    private toastr: ToastrService,
    private route: Router,
    private location: Location,public dialog: MatDialog) { }





  beforeunload($event) {
    $event.returnValue = "Are  you sure you want to continue?";
  }

  onPopState($event) {
    $event.returnValue = "Are  you sure you want to continue?";
  }

  onResize(event) {
    if ((window.innerWidth) <= 768) {
      $('.list-data').attr('style', 'display: none !important');
      $('.mob-left-list').attr('style', 'display: block !important');
      $('.btn-finalised').attr('style', 'display: block !important');

      this.mode = 'push';
      this.navOpened = false;
      this.disablePush = false;
    } else {
      this.mode = 'side';
      this.navOpened = true;
      this.disablePush = true;
      this.sideNav.opened = true;
    }
  }

  ngOnInit() {
    // $("#UAT").modal.show();
    $("#UAT").modal('show');
    $('.cont-main').css('min-height', $(window).height() - 150);
    $('.mat-drawer-container').css('height', $(window).height() - 150);

    this.activeRoute.params.subscribe(d => {
      this.getQuestionList(d.id);
    });
    if ((window.innerWidth) <= 768) {
      $('.list-data').attr('style', 'display: none !important');
      $('.mob-left-list').attr('style', 'display: block !important');
      $('.btn-finalised').attr('style', 'display: block !important');

      this.mode = 'push';
      this.navOpened = false;
      this.disablePush = false;
    } else {
      this.mode = 'side';
      this.navOpened = true;
      this.disablePush = true;
      this.sideNav.opened = true;
    }
  }
  getQuestionList(formId) {
    this.commonService.getQuestionList(formId).subscribe((data) => {
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
  submitClicked() {
    this.finalize = false;
    $('#submit-button').click();
  }

  async  finalizeClicked() {
    this.finalize = true;
    // // $('#submit-button').click();
    this.valid = false;
    for (let i = 0; i < this.formDetails.length; i++) {
      let section = this.formDetails[i]
      if (this.selectedSection !== section) {
        this.selectedSection = section;
        this.formSections = [];
        this.sectionName = section.name;
        this.formSections = section.questions;

        this.sdrcForm.questionArray = this.formSections;
        this.sdrcForm.numberOfColumn = 1;
        this.sdrcForm.ngOnChanges();

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
      await this.wait(100);
      $('#submit-button').click();
      if (!this.sdrcForm.form.valid && !this.sdrcForm.form.disabled) {
        return false;
      }
    }
    this.valid = true;
    $('#submit-button').click();
    return true;
    // this.saveForm(null);
  }

  wait(ms: number) {
    return new Promise((resolve) => {
      setTimeout(resolve, ms);
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
  clickSection(section) {
    this.selectedSection = section;
    this.formSections = [];
    this.sectionName = section.name;
    this.formSections = section.questions;
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
  saveForm(data) {

    const submissionData = this.makeNewFormList(this.formDetails);

    if (this.finalize && this.valid) {
      this.commonService.finalizeForm(submissionData).subscribe((data) => {
        if (data.statusCode === status.OK) {
          this.toastr.success(data.message, 'Success', {
          });
        } else if (data.statusCode === status.NOT_MODIFIED) {
          this.toastr.warning(data.message, 'Warning', {

          });
        } else if (data.statusCode === status.CONFLICT) {
          this.toastr.error(data.message, 'Error', {

          });
        }
        this.location.back();
      });
    } else if (!this.finalize) {
      this.commonService.saveFormData(submissionData).subscribe((data) => {
        if (data.statusCode === status.OK) {
          this.toastr.success(data.message, 'Success', {

          });
        } else if (data.statusCode === status.NOT_MODIFIED) {
          this.toastr.warning(data.message, 'Warning', {

          });
        } else if (data.statusCode === status.CONFLICT) {
          this.toastr.error(data.message, 'Error', {

          });
        }
        this.location.back();
      });
    }
  }
  makeNewFormList(formlist) {
    const newformlist = [];
    for (const item of formlist) {
      // tslint:disable-next-line:prefer-for-of
      for (let index = 0; index < item.questions.length; index++) {
        newformlist[newformlist.length] = Object.assign(item.questions[index]);
      }
    }
    return newformlist;
  }
  showLists() {
    $('.list-data').attr('style', 'display: block !important');
    $('.mob-left-list').attr('style', 'display: none !important');

  }
  showhide() {
    this.navShow = false;
  }
  showarrow() {
    this.navShow = true;
  }

  goback() {
    this.location.back();
  }

  viewRejectionReason(reason)
  {
    const dialogRef = this.dialog.open(InformationDialogComponent,
      { width: '400px', data: { msg: reason } });
  }
}

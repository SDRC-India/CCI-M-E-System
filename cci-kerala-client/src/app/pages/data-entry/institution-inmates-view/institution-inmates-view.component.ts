import { Component, OnInit } from '@angular/core';
import { CommonService } from '../service/common.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'scps-institution-inmates-view',
  templateUrl: './institution-inmates-view.component.html',
  styleUrls: ['./institution-inmates-view.component.scss']
})
export class InstitutionInmatesViewComponent implements OnInit {

  formDetails: any[] = [];
  formSections: any = [];
  sectionName: any;
  formId = 0;
  selectedSection: any;
  constructor(private commonService: CommonService, private activeRoute: ActivatedRoute,
   private location: Location,private appService:AppService ) { }

  ngOnInit() {
    this.activeRoute.params.subscribe(d => {
      this.formId=d.id;
      this.getQuestionList(d.id);
    }
    );
  }
  htmltoPDF() {
    this.appService.htmltoPDF(this.formDetails,this.formId);
  }

  getQuestionList(formId) {
    this.commonService.getQuestionListForView(formId).subscribe((data) => {
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
  goback(){
    this.location.back();
  }

}

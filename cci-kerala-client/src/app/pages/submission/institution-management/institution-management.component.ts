import { Component, OnInit } from '@angular/core';
import { SubmissionService } from '../service/submission.service';
import { Router } from '@angular/router';
import { Constants } from 'src/app/constants';

@Component({
  selector: 'scps-institution-management',
  templateUrl: './institution-management.component.html',
  styleUrls: ['./institution-management.component.scss']
})
export class InstitutionManagementComponent implements OnInit {
  tableData: any;
  tableColumns: string[];
  cciInfo: any = [];
  cciKeys: any;

  constructor(private submissionService: SubmissionService, private router: Router) { }

  ngOnInit() {
    this.submissionService.getInmatesManamentData(Constants.FORM_ID_INSTITUTION).subscribe((data) => {
      this.tableData = data.tableData;
      this.tableColumns = data.tableColumn;
    });
  }
  clickToNavigate(id: number) {
    this.router.navigate(['/ccihome/dataEntry', id]);
  }
  actionClicked(rowObj: any) {
    this.submissionService.submissionId = rowObj.rowObj.id;
    if (rowObj.target.includes('pending-view')) {
      this.router.navigate(['/submission/view-details', Constants.FORM_ID_INSTITUTION ]);
    }
  }
}
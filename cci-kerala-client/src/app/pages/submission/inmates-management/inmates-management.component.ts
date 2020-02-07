import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SubmissionService } from '../service/submission.service';
import { Constants } from 'src/app/constants';

@Component({
  selector: 'scps-inmates-management',
  templateUrl: './inmates-management.component.html',
  styleUrls: ['./inmates-management.component.scss']
})
export class InmatesManagementComponent implements OnInit {
  tableData: any;
  tableColumns: string[];
  cciInfo: any = [];
  cciKeys: any;

  constructor(private submissionService: SubmissionService, private router: Router) { }

  ngOnInit() {
    this.submissionService.getInmatesManamentData(Constants.FORM_ID_INMATE).subscribe((data) => {
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
      this.router.navigate(['/submission/view-details', Constants.FORM_ID_INMATE ]);
    }
  }
}

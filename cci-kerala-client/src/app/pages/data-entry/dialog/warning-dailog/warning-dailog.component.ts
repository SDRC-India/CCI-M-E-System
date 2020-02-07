import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'scps-warning-dailog',
  templateUrl: './warning-dailog.component.html',
  styleUrls: ['./warning-dailog.component.scss']
})
export class WarningDailogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

}

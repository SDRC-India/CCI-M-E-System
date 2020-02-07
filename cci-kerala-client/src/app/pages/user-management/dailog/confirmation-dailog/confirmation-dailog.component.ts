import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'scps-confirmation-dailog',
  templateUrl: './confirmation-dailog.component.html',
  styleUrls: ['./confirmation-dailog.component.scss']
})
export class ConfirmationDailogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}


}

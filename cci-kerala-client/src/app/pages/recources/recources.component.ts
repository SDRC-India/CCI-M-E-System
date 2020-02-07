import { Component, OnInit } from '@angular/core';
import saveAs from "save-as";
import { Constants } from 'src/app/constants';

@Component({
  selector: 'scps-recources',
  templateUrl: './recources.component.html',
  styleUrls: ['./recources.component.scss']
})
export class RecourcesComponent implements OnInit {
  resources: any=[
    {
    "Particulars":"Admin User Manual - English",
    "download": "Admin_User Manual.pdf"
    },
    {
      "Particulars":"CCI User Manual - English",
      "download": "CCI_User Manual.pdf"
    },
    {
      "Particulars":"DCPU User Manual - English",
      "download": "DCPU_User Manual.pdf"
    },
    {
      "Particulars":"State User Manual - English",
      "download": "State_User Manual.pdf"
    }
  ];
  constructor() { }
  ngOnInit() {
  }

  download(fileName)
  {
    let link = document.createElement("a");
    link.download = fileName;
    link.href = Constants.RESOURCE_FOLDER+fileName;
    link.click()

    // window.open(fileName,"_blank");
    // saveAs(Constants.RESOURCE_FOLDER+fileName,fileName);
  }
}

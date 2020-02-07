import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'scps-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  router: Router;
  noOfVisitors: number;
  constructor(router: Router) {
    this.router = router;
   }

  ngOnInit() {
   this.getNoOfVisitors();
  }

  getNoOfVisitors(){
    // this.http.get(Constants.HOME_URL+'bypass/getVisitorCount').subscribe(res=>{
    //   this.noOfVisitors = res as number;
    // })
  }
}

import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { LoadingBarService } from '@ngx-loading-bar/core';
declare var $: any;
@Component({
  selector: 'scps-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  ngOnInit() {
    $(".main-content, .cont-main").css("min-height", $(window).height() - 150)
  }

  constructor(public loader: LoadingBarService, private spinner: NgxSpinnerService) {
    this.loader.progress$.subscribe(data => {
      if (data > 0) {
        this.spinner.show();
      } else {
        this.spinner.hide();
      }
    });
  }

  ngAfterViewChecked() {
    if ($(window).width() <= 992) {
      $(".collapse").removeClass("show");
      $(".navbar-nav .nav-item").not('.dropdown').click(function () {
        $(".collapse").removeClass("show");
      })
    }
    $(".main-content").css("min-height", $(window).height() - 161);
  }

}

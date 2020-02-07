import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { AppService } from 'src/app/app.service';
import { Title } from '@angular/platform-browser';
import { filter, map, mergeMap } from 'rxjs/operators';
import { NotificationModel } from 'src/app/models/notification-model';
import { NewsUpdateService } from '../../../services/news-update.service';
import { Subscription } from 'rxjs';
declare var $: any;

@Component({
  selector: 'scps-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  router: Router;
  app: any;
  userLevel: string;
  userName: string;
  homeLangDetails: any[] = [];
  homeData: any;
  homeLang: any[] = [];
  multiLang: any[] = [];
  english: string= 'English';
  fontIncrement: number = 0;
  notification:NotificationModel;
  allNewsUpdates : NewsModel[]=[];
  newsUpdateService:NewsUpdateService;
  messages: any[] = [];
  subscription: Subscription;
  constructor( router: Router, private appService: AppService, private activatedRoute: ActivatedRoute,private titleService:Title, private newsUpdateProvider:NewsUpdateService) {
    this.router = router;
    this.app = appService;
    this.newsUpdateService = newsUpdateProvider;
    router.events.subscribe((url:any) => {
      this.fontIncrement = 0;
    });
    this.subscription = this.newsUpdateService.getMessage().subscribe(message => {
      if (message) {
        this.getAllNewsUpdates();
      } else {
        this.messages = [];
      }
    });
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(() => this.activatedRoute),
      map((route) => {
        while (route.firstChild) {
          route = route.firstChild;
        };
    
        return route;
      }),
      filter((route) => route.outlet === 'primary'),
      mergeMap((route) => route.data),
    ).subscribe((event) =>{
      this.titleService.setTitle(event['title'])
      if(this.app.checkUserAuthorization(['submissionManagement']))
      {
        this.appService.getNotification().subscribe(data=>{
          this.notification=data;
        })
      }

      if(this.appService.checkLoggedIn())
      {
        this.getAllNewsUpdates();
      }
    });

  }

  ngOnInit() {
    $(window).scroll(() => {
      const sticky = $('.sticky');
      const scroll = $(window).scrollTop();
      if (scroll >= 100){
        sticky.addClass('fixed');
      } else{
        sticky.removeClass('fixed');
      }
    });
  }

  setFontSize(condition) {
    if(condition == "plus" && this.fontIncrement <=2){
      this.fontIncrement++;
      $(".clsContent *").each(function() {
        $(this).css("font-size",parseInt( $(this).css("font-size").split("px")[0])+2+"px")
      })
    }
    if(condition =="minus" && this.fontIncrement >= -2){
      this.fontIncrement--;
      $(".clsContent *").each(function() {
        $(this).css("font-size",parseInt( $(this).css("font-size").split("px")[0])-2+"px")
      })
    }
    if(condition == "default"){
      let tempThis = this;
      $(".clsContent *").each(function() {
        $(this).css("font-size",parseInt( $(this).css("font-size").split("px")[0])-(2*tempThis.fontIncrement)+"px")
      })
      this.fontIncrement = 0;
    }

  }

  switchToLanguage(index) {
    // if(index == 0){
    //   this.staticService.selectedLang = 'odia';
    // }
    // else {
    //   this.staticService.selectedLang = 'english';
    // }
    // // if(this.staticService.contentData.viewName == 'Home'){
    //   this.staticService.reinitializeData(this.staticService.contentData);
    // // }
    // // else {
    //   this.staticPageService.reinitializeData(this.staticPageService.contentData);
    // // }
  }

  logout(){
    this.appService.logout();
    this.app.userName = "";
    $("#logout-success").fadeIn()
    setTimeout(() => {
      $("#logout-success").fadeOut('slow')
    }, 2000);
  }

  contactSection(){
    // $(window).animate({ scrollTop: $("#contact").offset().top});
    $(window).scrollTop( $("#contact").offset().top);
  }
  pendingNotification(){
    var popup = document.getElementById("myPopup");
    popup.classList.toggle("show");
  }
  InmatesPage(inmate){
    if(inmate == this.notification.inmatesNo){
      this.router.navigate(['/submission/inmates-management']);
    }
  }
 

  getAllNewsUpdates(){
    this.newsUpdateService.getAllNewsUpdates().subscribe(res => {
      this.allNewsUpdates = res as NewsModel[];
    },err => {
      console.log(err);
    })
  }
  appendHttp(link:string){
    if(!link.startsWith('http')){
      return 'http://'+link;
    }
    else{
      return link;
    }
  }

}

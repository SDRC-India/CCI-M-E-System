import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import {Location} from '@angular/common';
import { AppService } from 'src/app/app.service';
import { Constants } from 'src/app/constants';

@Injectable({
  providedIn: 'root'
})
export class LoggedinGuard implements CanActivate {
  constructor(private app: AppService, private router: Router, private location: Location) {

  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      if (!this.app.checkLoggedIn()) {
        return true;
      } else {
      let details= JSON.parse(localStorage.getItem(Constants.USER_DETAILS))
        this.router.navigateByUrl(details.sessionMap.landing);
      }
  }
}
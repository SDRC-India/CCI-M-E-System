import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AppService } from 'src/app/app.service';
import { Constants } from 'src/app/constants';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private app: AppService, private router: Router) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const expectedRoles = next.data.expectedRoles;

    if(this.app.checkLoggedIn()){
      const token = JSON.parse(localStorage.getItem(Constants.USER_DETAILS));
      let flag = false;
      expectedRoles.forEach(expectedRole => {
       for ( let i = 0; i < token.authorities.length; i++ ) {
        if (token.authorities[i] === expectedRole) {
          flag = true;
        }
       }
      });
      return flag;
    } else {
      this.router.navigate(['/exception']);
      return false;
    }
  }
}
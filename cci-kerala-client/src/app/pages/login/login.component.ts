import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AppService } from 'src/app/app.service';
import { Router } from '@angular/router';
declare var $: any;
@Component({
  selector: 'scps-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  hide = true;
  credentials: any = {
    username: '',
    password: ''
  };
  newPassword: any;
  confirmPassword: any;
  form: FormGroup;
  app: AppService;
  constructor( private appService: AppService, private router: Router, private frmbuilder: FormBuilder) { }

  ngOnInit() {
    this.app = this.appService;
  }
  login() {
    this.app.authenticate(this.credentials, () => {
      if (this.app.authenticated === true) {
        this.router.navigateByUrl('/ccihome');
      } else {
        $('.error-message').fadeIn('slow');
        setTimeout( () => {
          $('.error-message').fadeOut('slow');
        }, 5000);
      }
    });
    return false;
  }
}

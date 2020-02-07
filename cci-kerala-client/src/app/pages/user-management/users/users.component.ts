import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material';
import { Constants } from 'src/app/constants';
import { FormGroup, FormBuilder, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { ChangePasswordComponent } from '../dailog/change-password/change-password.component';
import * as status from 'http-status-codes';
import { ConfirmationDailogComponent } from '../dailog/confirmation-dailog/confirmation-dailog.component';

@Component({
  selector: 'scps-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  tableData: any;
  tableColumns: string[];
  zoom: number = Constants.DEFAULT_ZOOM;
  area: string;

  totalData: any;


  userFormGroup: FormGroup;
  cciUserFormGroup: FormGroup;
  stateList: any;
  districtList: any;

  constructor(private userService: UserService,
    private router: Router, private toastr: ToastrService, private dialog: MatDialog,
    private fb: FormBuilder) {
    this.userFormGroup = this.fb.group({
      state: new FormControl('', [Validators.required]),
      username: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.email])
    });
    this.cciUserFormGroup = this.fb.group({
      state: new FormControl('', [Validators.required]),
      district: new FormControl('', [Validators.required]),
      cciname: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.email])
    });

  }

  ngOnInit() {
    this.getUserList();
    this.getAreaLevel();
  }
  getAreaLevel() {
    this.userService.getAreaLevel().subscribe((data) => {
      this.stateList = data['state'];
      if (this.stateList.length <= 1) {
        this.userFormGroup.controls['state'].disable();
        this.userFormGroup.controls['state'].setValue(this.stateList[0].areaId);
        this.cciUserFormGroup.controls['state'].disable();
        this.cciUserFormGroup.controls['state'].setValue(this.stateList[0].areaId);
      }
      this.districtList = data['district'];
    });
  }
  getUserList() {
    this.userService.getAllUsers().subscribe((data) => {
      // this.totalData = data.tableData;
      this.tableData = data;
      // this.tableColumns = data.tableHeader;
    });
  }

  actionClicked(rowObj: any) {
    const userId = rowObj.rowObj.id;
    if (rowObj.target.includes('disable')) {

      const dialogRef = this.dialog.open(ConfirmationDailogComponent,
        { width: '400px', data: { msg: 'Do you want to disable this user?',button:'Disable' } });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.userService.disableUser(userId).subscribe(
            data => {
              this.toastr.success(data);
              this.getUserList();
            }
          );
        }
      }
      );
    } else if (rowObj.target.includes('enable')) {

      const dialogRef = this.dialog.open(ConfirmationDailogComponent,
        { width: '400px', data: { msg: 'Do you want to enable this user?',button:'Enable' } });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.userService.enableUser(userId).subscribe(
            data => {
              this.toastr.success(data);
              this.getUserList();
            }
      );
          }
      }
      );
    } else if (rowObj.target.includes('reset-pass')) {
      const dialogRef = this.dialog.open(ChangePasswordComponent, { data: { id : userId}});
      dialogRef.afterClosed().subscribe(d => {
       if(d)
       {
        this.toastr.success('Password has been changed sucessfully'); 
       }
      })
    }
  }

  reset(type)
  {
    switch(type)
    {
      case 'CCI':
      this.cciUserFormGroup.reset();
      if(this.cciUserFormGroup.controls['state'].disabled)
       this.cciUserFormGroup.controls['state'].setValue(this.stateList[0].areaId);
      // this.cciUserFormGroup.markAsUntouched();
      break;

      case 'User':
      this.userFormGroup.reset();
      if(this.userFormGroup.controls['state'].disabled)
      this.userFormGroup.controls['state'].setValue(this.stateList[0].areaId);
      //this.userFormGroup.markAsUntouched();
      break;
    }

  }
  validateForEdit(rowObj) {
  }
  userSubmit(formDirective: FormGroupDirective) {
    if (this.userFormGroup.valid) {
      let userData ={
        userName:this.userFormGroup.controls['username'].value,
        areaId:this.userFormGroup.controls['state'].value,
        email:this.userFormGroup.controls['email'].value
      }
      this.userService.submitUser(userData).subscribe((msg) => {
        if(msg.statusCode==status.OK)
      {
        formDirective.resetForm();
        this.userFormGroup.reset();
        if(this.userFormGroup.controls['state'].disabled)
        this.userFormGroup.controls['state'].setValue(this.stateList[0].areaId);

        this.getUserList();
        this.toastr.success(msg.message);
      }

      else if(msg.statusCode==status.BAD_REQUEST)
      this.toastr.error(msg.message)
      });
    }
  }
  cciSubmit(formDirective: FormGroupDirective) {
    if (this.cciUserFormGroup.valid) {
      let cciData ={
        districtId:this.cciUserFormGroup.controls['district'].value,
        cciName:this.cciUserFormGroup.controls['cciname'].value,
        email:this.cciUserFormGroup.controls['email'].value
      }

      this.userService.submitCCI(cciData).subscribe((msg) => {
        if(msg.statusCode==status.OK)
        {
          formDirective.resetForm();
          this.cciUserFormGroup.reset();
         if(this.cciUserFormGroup.controls['state'].disabled)
          this.cciUserFormGroup.controls['state'].setValue(this.stateList[0].areaId);
          this.getUserList();
          this.toastr.success(msg.message);
        }

        else if(msg.statusCode==status.BAD_REQUEST)
        this.toastr.error(msg.message)
        
      });
    }
  }
}

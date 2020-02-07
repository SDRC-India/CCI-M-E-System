import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators, AbstractControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'scps-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;
  userId: any;
  constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<ChangePasswordComponent>,
              private userService: UserService, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.changePasswordForm = this.fb.group({
      password: new FormControl('', [Validators.required]),
      confirmPassword: new FormControl('', [Validators.required, this.validatePassword])
    });
  }
  validatePassword(control: AbstractControl) {
    const password = control.root.get('password') != null ? control.root.get('password').value : '';
    const confirmPassword = control.root.get('confirmPassword') != null ? control.root.get('confirmPassword').value : '';
    if (confirmPassword.length <= 0) {
      return null;
    }
    if (confirmPassword !== password) {
      return {
        doesMatchPassword: true
      };
    }
    return null;
  }
  ngOnInit() {
  }
  changePassword() {
    if (this.changePasswordForm.valid) {
      const resetPassMap = {
        userId : this.data.id,
        newPassword : this.changePasswordForm.controls['password'].value
      };

      this.userService.changePassword(resetPassMap).subscribe((msg) => {
        this.dialogRef.close(msg);
      });
    }
  }
}

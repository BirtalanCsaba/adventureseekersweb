import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/common/entity/user.entity';
import { GlobalValidators } from 'src/app/common/validation/global.validators';
import { AuthService } from 'src/app/services/auth.service';
import { NotificationService } from 'src/app/services/notification.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'security',
  templateUrl: './security.component.html',
  styleUrls: ['./security.component.css']
})
export class SecurityComponent implements OnInit {

  currentEmail : String | undefined;


  changeEmailForm = new FormGroup({
    email: new FormControl('', 
    [
      GlobalValidators.cannotContainSpace,
      Validators.required,
      Validators.email,
    ])
  })

  get email() {
    return this.changeEmailForm.get('email');
  }

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private toastrService: NotificationService
  ) { }

  ngOnInit(): void {
    this.userService.getUser(this.authService.currentUser.sub)
      .subscribe(response => {
        this.currentEmail = response.Email ? response.Email : "";
      });
  }

  updateEmail() {
    if (this.changeEmailForm.valid) {
      let theUser = new User();
      theUser.Email = this.email?.value;
      this.userService.existsEmail(theUser.Email ? theUser.Email : "")
        .subscribe(response => {
          if (response) {
            this.email?.setErrors({
              shouldBeUnique: true
            });
          }
          else {
            console.log(theUser);
            this.userService.patchUser(this.authService.currentUser.sub, theUser)
              .subscribe(response => {
                this.toastrService.showSuccess("Email changed", "Adventure Seekers");
              });
          }
        });
    }
  }
}

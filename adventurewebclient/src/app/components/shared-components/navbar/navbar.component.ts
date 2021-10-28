import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { faBell } from '@fortawesome/free-regular-svg-icons';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { faUsers } from '@fortawesome/free-solid-svg-icons';
import { faCog } from '@fortawesome/free-solid-svg-icons';
import { faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import { UserService } from 'src/app/services/user.service';
import { UserDetail } from 'src/app/common/entity/user-detail.entity';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  faBell = faBell;
  faUser = faUser;
  faUsers = faUsers;
  faCog = faCog;
  faSignOutAlt = faSignOutAlt;

  profileImage: Blob | String | SafeUrl | undefined;

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      let userDetail = new UserDetail();
      this.userService
        .getUserDetails(this.authService.currentUser.sub)
        .subscribe((response: UserDetail) => {
          userDetail = response;

          if (userDetail.ProfileImage != null) {
            let objectURL = 'data:image/jpeg;base64,' + userDetail.ProfileImage;
            this.profileImage = this.sanitizer.bypassSecurityTrustUrl(objectURL);

            //this.profileImage = userDetail.ProfileImage;
          }
          else {
            this.profileImage = "/assets/images/man-avatar.svg";
          }
        });
      
    }
  }

}

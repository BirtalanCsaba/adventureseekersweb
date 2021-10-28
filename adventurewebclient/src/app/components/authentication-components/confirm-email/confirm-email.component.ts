import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-confirm-email',
  templateUrl: './confirm-email.component.html',
  styleUrls: ['./confirm-email.component.css']
})
export class ConfirmEmailComponent implements OnInit {

  errorMessage = "";
  isConfirmed = true;
  canResend = false;

  constructor(
    private authService: AuthService,
    private activatedRouter: ActivatedRoute,
    private router: Router,
    private toastr: NotificationService) { }

  ngOnInit(): void {
    this.activatedRouter.paramMap
      .subscribe((result: any) => {
        let token = result.get('token');
        this.authService.confirmToken(token)
          .subscribe((result: any) => {
            this.toastr.showSuccess("Account confirmed", "Adventure Seekers Romania");
            this.router.navigate(['/login']);
          },
          (error: any) => {
            if(error.status === 400) {
              this.isConfirmed = false;
              this.errorMessage = error?.error.message;
              if(this.errorMessage !== "Account already confirmed") {
                this.canResend = true;
              }
            } else {
              throw error;
            } 
          });
      });
  }

  resendVerificationEmail(input: any) {
    this.authService.resendToken(input.email)
      .subscribe((result: any) => {
        this.toastr.showSuccess("Verification email sent", "Adventure Seekers Romania");
        this.router.navigate(['/login']);
      },
      (error: any) => {
        if (error.status !== 400) {
          throw error;
        }
        this.router.navigate(['/login']);
      });
  }

}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  invalidLogin = false;

  constructor(
    private authService: AuthService,
    private router: Router) { }

  ngOnInit(): void {
    
  }

  signIn(credentials: any) {
    this.authService.login(credentials)
      .subscribe(result => {
        if(result)
          this.router.navigate(['/']);
        else
          this.invalidLogin = true;
      },
      (error: Response) => {
        if(error.status == 403)
          this.invalidLogin = true;
        else 
          throw error;
      });
  }
}

import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/shared-components/navbar/navbar.component';
import { FooterComponent } from './components/shared-components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppErrorHandler } from './common/app-error-handler';
import { LoginComponent } from './components/authentication-components/login/login.component';
import { RegisterComponent } from './components/authentication-components/register/register.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { ConfirmEmailComponent } from './components/authentication-components/confirm-email/confirm-email.component';
import { ConfirmEmailNotificationComponent } from './components/authentication-components/confirm-email-notification/confirm-email-notification.component';
import { ErrorPageComponent } from './components/shared-components/error-page/error-page.component';
import { UserProfileComponent } from './components/account/settings/user-profile/user-profile.component';
import { AuthGuard } from './services/guards/auth-guard.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserManageComponent } from './components/account/user-manage/user-manage.component';
import { MyAccountComponent } from './components/account/settings/my-account/my-account.component';
import { SecurityComponent } from './components/account/settings/security/security.component';
import { EmailNotificationsComponent } from './components/account/settings/email-notifications/email-notifications.component';
import { CookiesComponent } from './components/account/settings/cookies/cookies.component';
import { LanguageComponent } from './components/account/settings/language/language.component';
import { AddsComponent } from './components/account/settings/adds/adds.component';
import { SupportComponent } from './components/account/settings/support/support.component';
import { JwtModule } from "@auth0/angular-jwt";
import { TokenHelper } from './services/local-storage/token.helper';
import { ToggleButtonComponent } from './components/shared-components/controls/toggle-button/toggle-button.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    ConfirmEmailComponent,
    ConfirmEmailNotificationComponent,
    UserProfileComponent,
    SecurityComponent,
    UserManageComponent,
    EmailNotificationsComponent,
    CookiesComponent,
    LanguageComponent,
    SupportComponent,
    MyAccountComponent,
    ToggleButtonComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbTooltipModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: TokenHelper.getAccessToken,
        allowedDomains: ['localhost:8080'],
        skipWhenExpired: true
      },
    }),
    BrowserAnimationsModule,
    BsDatepickerModule.forRoot(),
    ToastrModule.forRoot({
      timeOut: 5000,
      preventDuplicates: true
    }),
    RouterModule.forRoot([
      { path: '', component: HomeComponent }, 
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      { path: 'confirm/:token', component: ConfirmEmailComponent },
      { path: 'confirm-notification', component: ConfirmEmailNotificationComponent },
      { 
        path: 'settings/account', 
        component: UserManageComponent,
        canActivate: [AuthGuard],
        children:[
          {
            path: '',
            component: MyAccountComponent
          },
          {
            path: 'security',
            component: SecurityComponent
          },
          {
            path: 'email-notifications',
            component: EmailNotificationsComponent
          },
          {
            path: 'cookies',
            component: CookiesComponent
          },
          {
            path: 'language',
            component: LanguageComponent
          },
          {
            path: 'adds',
            component: AddsComponent
          },
          {
            path: 'support',
            component: SupportComponent
          }
        ]
      },
      { 
        path: 'profile', 
        component: UserProfileComponent,
        canActivate: [AuthGuard]
      },
      { path: '**', component: ErrorPageComponent },
    ]),
    FontAwesomeModule,
    
  ],
  providers: [
    { provide: ErrorHandler, useClass: AppErrorHandler }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

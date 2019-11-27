import { BrowserModule } from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import { AppComponent } from './app.component';
import {initializer} from './utils/app-init';
import {KeycloakAngularModule, KeycloakService} from 'keycloak-angular';
import { MainviewComponent } from './mainview/mainview.component';
import { RouterModule, Routes } from '@angular/router';
import { SuperuserComponent } from './mainview/superuser/superuser.component';
import { WelcomeComponent } from './welcome/welcome.component';
import {AppAuthGuard} from './mainview/app.authguard';
import { HttpClientModule } from '@angular/common/http';

const appRoutes: Routes = [
  { path: 'mainview', component: MainviewComponent, canActivate: [AppAuthGuard]},
  { path: 'superuser', component: SuperuserComponent, canActivate: [AppAuthGuard], data: {roles: ['superuser']}},
  { path: 'welcome', component: WelcomeComponent },
  { path: '**',
    redirectTo: 'welcome',
    pathMatch: 'full'
  }
];

@NgModule({
  declarations: [
    AppComponent,
    MainviewComponent,
    SuperuserComponent,
    WelcomeComponent
  ],
  imports: [
    BrowserModule,
    KeycloakAngularModule,
    RouterModule.forRoot(
      appRoutes,
      { useHash: true, enableTracing: false } // <-- debugging purposes only
    ),
    HttpClientModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializer,
      multi: true,
      deps: [KeycloakService],
    },
    AppAuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

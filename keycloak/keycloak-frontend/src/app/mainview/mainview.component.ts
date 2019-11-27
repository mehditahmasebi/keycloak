import { Component, OnInit } from '@angular/core';
import {KeycloakService} from 'keycloak-angular';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-mainview',
  templateUrl: './mainview.component.html',
  styleUrls: ['./mainview.component.css']
})
export class MainviewComponent implements OnInit {

  constructor(private keycloakService: KeycloakService,
              private http: HttpClient) { }
  username: string;
  roles: string[];
  privateMessage: string = 'NOT CALLED';
  superUserMessage: string = 'NOT CALLED';

  ngOnInit() {
    try {
      this.username = this.keycloakService.getUsername();
      this.roles = this.keycloakService.getUserRoles();
      // this.keycloakService.loadUserProfile(true).then()
    } catch (e) {
      console.log('Failed to load user details', e);
    }
    this.http.get("http://localhost:8080/private",{responseType: "text"})
      .subscribe(
        resp => {
            this.privateMessage = resp;
          },
          err=> {
            this.privateMessage = "ERROR";
        });
    this.http.get("http://localhost:8080/private/superuser",{responseType: "text"})
      .subscribe(
        resp => {
          this.superUserMessage = resp;
        },
        err=> {
          this.superUserMessage = "ERROR";
        });
  }

}

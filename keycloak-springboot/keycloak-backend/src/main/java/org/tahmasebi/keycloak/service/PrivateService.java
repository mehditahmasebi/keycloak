package org.tahmasebi.keycloak.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class PrivateService {

    @GetMapping("/private")
    public String helloPrivate(HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        System.out.println("Username : " + accessToken.getPreferredUsername());
        System.out.println("emailID : " + accessToken.getEmail());
//        lastname = accessToken.getFamilyName();
//        firstname = accessToken.getGivenName();
//        realmName = accessToken.getIssuer();
//        Access realmAccess = accessToken.getRealmAccess();
//        System.out.println("Roles : " + accessToken.getRealmAccess().);
        System.out.println("Roles : " + accessToken.getRealmAccess().getRoles().toString());
        System.out.println("Group : " + accessToken.getOtherClaims().entrySet());
        return "Hello Private for any user, for more: /private/superuser";
    }

    @GetMapping("/private/superuser")
//    @PreAuthorize("hasAnyAuthority('superuser')")
    @Secured("ROLE_SUPERUSER")
    public String superUser()
    {
        return "Super User Works!!";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

}

package org.tahmasebi.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    private final String serverUrl = "http://192.168.3.40:8080/auth";
    private final String realm = "mt-test";
    private final String clientId = "mt-app";
    //        private final String clientSecret = "288876a6-c469-4a58-bdbb-5aefa8fd82ab";

    @Bean
    public Keycloak keycloak() {
        //admin admin is a user of realm with [realm-management] Client Roles
            Keycloak keycloak = KeycloakBuilder.builder() //
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .username("admin")
                    .password("admin")
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                    .build();
        //Login with Admin of Keycloak
/*        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username("admin")
                .password("CRM@dp9711")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
                .build();*/

        return keycloak;
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak)
    {
        return keycloak.realm(realm);
    }

}

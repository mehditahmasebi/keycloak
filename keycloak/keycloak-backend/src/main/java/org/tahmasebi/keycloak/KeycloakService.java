package org.tahmasebi.keycloak;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeycloakService {
    @Autowired
    RealmResource realmResource;

    public List<String> getUserGroups(String username) {
        List<String> result = new ArrayList<>();;
        UserRepresentation userRepresentation = realmResource.users().search(username).get(0);
        UserResource userResource = realmResource.users().get(userRepresentation.getId());
        List<GroupRepresentation> groups = userResource.groups();
        if (groups != null && groups.size() > 0)
        {
            groups.forEach(gr -> result.add(gr.getName()));
        }
        return result;
    }
}

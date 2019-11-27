package org.tahmasebi.keycloak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeycloakApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Autowired
	RealmResource realmResource;
	@Test
	public void keycloakGetUser() {
			UsersResource usersResource = realmResource.users();
			System.out.println("User Count : " + usersResource.count());
			System.out.println("UserName[0] : " + usersResource.list().get(0).getUsername());
	}
	@Autowired
	KeycloakService keycloakService;
	@Test
	public void getUserGroups() {
		System.out.println(keycloakService.getUserGroups("admin"));
	}
}

need a keycloak user with (realms's) roles "user" and "superuser"

server side logout : /sso/logout

Keycloak server: 192.168.3.40

sample username: admin password: admin

Note: all roles in keycloak defined as Realm's role scope, so we can use use-resource-role-mappings = false

important note (need to check): 
1. When user logged in from client side and when want to logout, user must logout from client and backend separately
2. To solve this problem minimize Acess Token Lifespan for example to 2 minutes.
  

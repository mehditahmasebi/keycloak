import {KeycloakService} from 'keycloak-angular';


export function initializer(keycloak: KeycloakService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: {
            url: 'http://192.168.3.40:8080/auth',
            realm: 'mt-test',
            clientId: 'mt-app',
            'credentials': {
              'secret': '',
            },
          },
          initOptions: {
            onLoad: 'check-sso',
            /*onLoad: 'login-required',*/
            checkLoginIframe: false,
          },
          bearerExcludedUrls: [],
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}

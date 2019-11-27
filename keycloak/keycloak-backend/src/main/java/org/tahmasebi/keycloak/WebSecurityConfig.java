package org.tahmasebi.keycloak;//

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.filter.*;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableWebSecurity
@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true,
                            securedEnabled=true,
                            jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {

    public WebSecurityConfig() {
    }

    @Bean
    protected AdapterDeploymentContext adapterDeploymentContext() throws Exception {
        AdapterDeploymentContextFactoryBean factoryBean;
        ClassPathResource resource = new ClassPathResource("keycloak.json");
        factoryBean = new AdapterDeploymentContextFactoryBean(resource);


        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
        return new KeycloakAuthenticationEntryPoint(this.adapterDeploymentContext());
    }

    @Bean
    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        //below codes made .hasRole and @Secured workable
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = new KeycloakAuthenticationProvider();
        SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
        simpleAuthorityMapper.setPrefix("ROLE_");
        simpleAuthorityMapper.setConvertToUpperCase(true);
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(simpleAuthorityMapper);
        return keycloakAuthenticationProvider;
    }

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    @Bean
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(this.authenticationManagerBean());
        filter.setSessionAuthenticationStrategy(this.sessionAuthenticationStrategy());
        return filter;
    }

    @Bean
    protected KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
        return new KeycloakPreAuthActionsFilter(this.httpSessionManager());
    }

    protected KeycloakCsrfRequestMatcher keycloakCsrfRequestMatcher() {
        return new KeycloakCsrfRequestMatcher();
    }

    @Bean
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    @Bean
    protected KeycloakLogoutHandler keycloakLogoutHandler() throws Exception {
        return new KeycloakLogoutHandler(this.adapterDeploymentContext());
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    protected KeycloakSecurityContextRequestFilter keycloakSecurityContextRequestFilter() {
        return new KeycloakSecurityContextRequestFilter();
    }

    @Bean
    protected KeycloakAuthenticatedActionsFilter keycloakAuthenticatedActionsRequestFilter() {
        return new KeycloakAuthenticatedActionsFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().requireCsrfProtectionMatcher(this.keycloakCsrfRequestMatcher()).and();
        http.sessionManagement().sessionAuthenticationStrategy(this.sessionAuthenticationStrategy()).and()
            .addFilterBefore(this.keycloakPreAuthActionsFilter(), LogoutFilter.class)
            .addFilterBefore(this.keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(this.keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
            .addFilterAfter(this.keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(this.authenticationEntryPoint());
        http.logout().addLogoutHandler(this.keycloakLogoutHandler())
                .logoutUrl("/sso/logout").permitAll()
                .logoutSuccessUrl("/")
        .and()
            .authorizeRequests()
            .antMatchers("/private**").hasRole("USER")
            .antMatchers("/private/**").hasRole("USER")
            .anyRequest().permitAll()
        .and().csrf().disable();
    }
}

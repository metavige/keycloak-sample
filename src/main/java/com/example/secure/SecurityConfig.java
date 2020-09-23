package com.example.secure;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * 參考
 * https://medium.com/chikuwa-tech-study/spring-boot-%E7%AC%AC17%E8%AA%B2-spring-security%E7%9A%84%E9%A9%97%E8%AD%89%E8%88%87%E6%8E%88%E6%AC%8A-263afe44ac20
 * https://www.baeldung.com/spring-boot-keycloak
 * https://github.com/hendisantika/spring-boot-keycloak-sample
 */
@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  // region Fields

  // endregion

  // region Methods

  @Bean
  public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  /**
   * Registers the KeycloakAuthenticationProvider with the authentication manager.
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    final KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
    // 這邊在比對角色名稱的時候，會加入 "ROLE_"
    // 因為 keyCloak 在存放 Authority (ROLE) 的時候，會將傳入的 Role 加上 "ROLE_"
    provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
    auth.authenticationProvider(provider);
  }

  @Override protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
      .authorizeRequests()
      .antMatchers("/hello*").hasAnyRole("admin")
      .anyRequest().permitAll();
  }

  // endregion

}

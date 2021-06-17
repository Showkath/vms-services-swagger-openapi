package com.sap.it.ec.vms.services.security;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired XsuaaServiceConfiguration xsuaaServiceConfiguration;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // TODO Auto-generated method stub
    http.authorizeRequests()
        .antMatchers(
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            /*Probably not needed*/ "/swagger.json")
        .permitAll()
        .and()
        .cors()
        .and()
        .csrf()
        .disable();
  }

  /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {



      http.sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session is created by approuter
          .and()
          .authorizeRequests()
          // check for SAP IT D/C/I user has to be before any request authentication
          .antMatchers("/**")
          .access("@auth.isDCIUser()")
          .anyRequest()
          .permitAll();


  //        .authenticated()
  //        .and()
  //        .oauth2ResourceServer()
  //        .jwt()
  //        .jwtAuthenticationConverter(getJwtAuthenticationConverter()
  //        );
    }
    */

  /** Customizes how GrantedAuthority are derived from a SAP IT Jwt */
  Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
    TokenAuthenticationConverter converter =
        new TokenAuthenticationConverter(xsuaaServiceConfiguration);
    converter.setLocalScopeAsAuthorities(true);
    return converter;
  }
}

package com.optimagrowth.organization.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration // 1) @Configuration 설정
@EnableWebSecurity // 2) 전역 WebSecurity 구성 적용
@EnableGlobalMethodSecurity(jsr250Enabled = true) // 3) @RoleAllowed 활성화
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter { // 4) KeycloakWebSecurityConfigurerAdapter 확장

    @Override
    // 5) keyclock 인증 제공자(authentication provider) 등록
    //  ㅇ 모든 접근 규칙은 configure() 에서 정의
    //  ㅇ HttpSecurity 객체로 모든 접근 규칙을 구성하고 설정
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
            .anyRequest().authenticated(); // 조직서비스의 모든 URL을 인가된 사용자만 접근 허용
        http.csrf().disable();
    }

    @Autowired
    // 6) authentication provider 정의
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    // 7) 세션 인증 전략 정의
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    // 8) (Default) 스프링 시큐리티는 keyclock.json 파일을 찾음
    //  ㅇ Keycloak과 스프링 부트를 연동하는데 사용되는 클래스
    //  ㅇ Keycloak 관련 프로퍼티를 가져와 설정
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}

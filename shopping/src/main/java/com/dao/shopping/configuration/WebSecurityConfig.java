package com.dao.shopping.configuration;

import com.dao.shopping.exception.CustomAccessDeniedHandler;
import com.dao.shopping.exception.CustomAuthenticationEntryPoint;
import com.dao.shopping.utils.Endpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtFilterAuthentication jwtFilterAuthentication;
    private final LogoutService logoutService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurityConfig(JwtFilterAuthentication jwtFilterAuthentication,
                             LogoutService logoutService,
                             CustomAccessDeniedHandler accessDeniedHandler,
                             CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtFilterAuthentication = jwtFilterAuthentication;
        this.logoutService = logoutService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Endpoints.PUBLIC).permitAll()
                        .requestMatchers(Endpoints.SWAGGER_UI).permitAll()
                        .requestMatchers(Endpoints.USER).hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(Endpoints.ADMIN).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exception -> {
            exception.authenticationEntryPoint(authenticationEntryPoint);
            exception.accessDeniedHandler(accessDeniedHandler);
        });
        http.addFilterBefore(jwtFilterAuthentication, UsernamePasswordAuthenticationFilter.class);

        http.logout((logout) -> {
            logout.logoutUrl("/api/auth/logout").permitAll();
            logout.addLogoutHandler(logoutService);
            logout.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        });
        return http.build();

    }

}

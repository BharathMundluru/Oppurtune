package com.example.CC.Config;
import com.example.CC.JWT.JwtFilter;
import com.example.CC.Service.CustomOAuth2UserService;
import com.example.CC.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class SSOconfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return userRequest -> {
            OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);
            Map<String, Object> attributes = user.getAttributes();
            return new DefaultOAuth2User(
                    Collections.singletonList(new SimpleGrantedAuthority("USER")),
                    attributes,
                    "email" // or "name", depending on your logic
            );
        };
    }


    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("https://localhost:8081"));
                    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup","/Web","/public/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN","ROLE_ADMIN")
                        .requestMatchers("/user/**", "/profile").hasAnyAuthority("USER", "ROLE_USER","ROLE_ADMIN", "ADMIN")
                        .requestMatchers("/add", "/jobs/edit/**", "/jobs/update/**", "/jobs/delete/**").hasAnyAuthority("ADMIN","ROLE_ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JWT_TOKEN")
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService))
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                );

        return http.build();
    }
}

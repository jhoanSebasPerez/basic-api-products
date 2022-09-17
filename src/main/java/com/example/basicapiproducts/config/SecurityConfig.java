package com.example.basicapiproducts.config;

import com.example.basicapiproducts.exceptions.CustomAccessDeniedHandler;
import com.example.basicapiproducts.exceptions.UnauthorizedEntryPoint;
import com.example.basicapiproducts.jwt.JwtFilter;
import com.example.basicapiproducts.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    private CustomAccessDeniedHandler customAccessDeniedHandler;


    public SecurityConfig(UnauthorizedEntryPoint unauthorizedEntryPoint,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/auth/**", "/upload/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                http.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
                return http.build();
    }

    @Bean
    public JwtFilter getJwtFilter(){
        return new JwtFilter();
    }
}

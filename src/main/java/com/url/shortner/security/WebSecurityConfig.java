package com.url.shortner.security;

import com.url.shortner.security.jwt.JwtAuthenticationFilter;
import com.url.shortner.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //This bean tells Spring thst we have to use this userDetails and this passwordEncoder for authentication
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    //this defines which url's are protected and how requests are authenticated
    // and which filter runs in what order
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //csrf is a session based auth
        //and since jwt is stateless we don't need csrf
        //since there is no session, csrf protection is not required (Cross Site Request Forgery)
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/api/auth/**"
                ).permitAll()
                .requestMatchers(
                        "/api/urls/**"
                ).authenticated()
                .requestMatchers(
                        "/{shortUrl}"
                ).permitAll()
                .anyRequest().authenticated()
        );
        http.authenticationProvider(authenticationProvider());
        //what we are doing here?
        //add our custom jwtAuthenticationFilter() before inbuilt UsernamePasswordAuthenticationFilter.class
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

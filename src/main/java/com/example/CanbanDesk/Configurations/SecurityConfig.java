package com.example.CanbanDesk.Configurations;

import com.example.CanbanDesk.Services.CustomEmployeeDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{
    private final CustomEmployeeDetailsService customEmployeeDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        HttpSecurity httpSecurity = http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/registration/**", "/", "/resources/**", "/static/**", "/css/**", "bootstrap-5.3.0/dist/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/employee/**").hasRole("EMPL")
                .requestMatchers("/admin/**").hasRole("ADM")
                .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/hello").failureUrl("/loginError").permitAll())
                        .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()

        );
        return http.build();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customEmployeeDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(8);
}
}

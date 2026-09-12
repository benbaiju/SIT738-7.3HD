package edu.deakin.sit738.finsight.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final boolean SPRING_CSRF_ENABLED = true;

    @Autowired
    private FinSightAuthenticationProvider authenticationProvider;

    @Autowired
    private FinSightAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (SPRING_CSRF_ENABLED) {
            http.csrf();
        } else {
            http.csrf().disable();
        }

        http
            .authorizeRequests()
                .antMatchers(
                        "/",
                        "/login",
                        "/register",
                        "/error.jsp",
                        "/error")
                .permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/advisor/**").hasRole("ADVISOR")
                .antMatchers("/my-advisor/**").hasRole("USER")
                .antMatchers(
                        "/dashboard",
                        "/expenses/**",
                        "/financial-insights",
                        "/investments/**",
                        "/loans/**",
                        "/goals/**",
                        "/transactions/**",
                        "/upload/**",
                        "/remote-import/**")
                .hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout().disable()
            .exceptionHandling()
                .accessDeniedPage("/error.jsp");
    }
}

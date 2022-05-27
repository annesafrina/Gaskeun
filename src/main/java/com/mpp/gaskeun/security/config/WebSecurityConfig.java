package com.mpp.gaskeun.security.config;

import com.mpp.gaskeun.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // So post request from other website can be processed
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/*",
                        "/registration/*",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/actuator/*",
                        "/",
                        "/explore",
                        "/explore/*",
                        "/static/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                .usernameParameter("email")
                .permitAll()
                .loginPage("/login")
                .defaultSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(authenticationService);

        return provider;
    }
}

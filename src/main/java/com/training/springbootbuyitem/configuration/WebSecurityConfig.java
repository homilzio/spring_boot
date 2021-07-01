package com.training.springbootbuyitem.configuration;

import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    //@Autowired
    //private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

  /*  @Override
    protected void configure(HttpSecurity http) throws Exception {

       http.csrf().disable().authorizeRequests()
                .antMatchers("/items/**").hasAnyRole(Role.ADMIN, Role.USER)
                .antMatchers("/shoppingCart/**").permitAll()
                .antMatchers("/users/allUsers").hasAnyRole(Role.ADMIN, Role.USER)
                .antMatchers("/users/infoAuth").permitAll()
                .antMatchers("/users/updateUser").hasAnyRole(Role.ADMIN, Role.USER)
                .antMatchers("/users/logoutAllUsers").hasAnyRole(Role.ADMIN)
                .antMatchers("/users/logout").hasAnyRole(Role.ADMIN, Role.USER)
                .and().httpBasic()
                .and().authorizeRequests()
                .antMatchers("/users/createUser").permitAll();
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }*/

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/items/**").permitAll()
                .antMatchers("/authenticate", "/users/createUser", "/users/infoAuth", "/shoppingCart/**").permitAll()
                .antMatchers("/users/logoutAllUsers").hasAnyRole(Role.ADMIN)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

        httpSecurity.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);


        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Map<String, CustomSessionManagerImpl> customSessionManagerMap() {return new HashMap<String, CustomSessionManagerImpl>() {
        private static final long serialVersionUID = 8906436286015294182L;
    };}

}
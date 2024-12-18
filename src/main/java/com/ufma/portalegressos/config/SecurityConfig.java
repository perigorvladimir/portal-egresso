package com.ufma.portalegressos.config;

import com.ufma.portalegressos.application.usecases.SenhaEncoder;
import com.ufma.portalegressos.infra.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // Define acesso restrito para a role ADMIN
                .antMatchers("/coordenador/**").hasRole("ROLE_COORDENADOR") // Define acesso restrito para a role ROLE_COORDENADOR
                .anyRequest().permitAll() // Permitir acesso a todas as outras URLs
                .and()
                .formLogin().loginPage("/login").permitAll() // Configura a página de login personalizada
                .and()
                .logout().permitAll(); // Permitir logout para todos
    }
}

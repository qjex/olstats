package xyz.qjex.olstats.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by qjex on 8/25/16.
 */

@Configuration
@EnableWebSecurity
@PropertySource("file:./config.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${admin.username}")
    private String userName;

    @Value("${admin.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/lists/**", "/admin/**").authenticated()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?error")
                .and()
            .logout()
                .logoutUrl("/login?logout")
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(userName).password(password).roles("USER");
    }
}

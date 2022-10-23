package accountservice.security;

import accountservice.exceptions.AccessDeniedHandlerImpl;
import accountservice.exceptions.AuthenticationEntryPointImpl;
import accountservice.user.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    private AccessDeniedHandlerImpl accessDeniedHandler;
    private AuthenticationEntryPointImpl authenticationEntryPoint;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getBCryptPasswordEncoder());

        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles()
                .and().passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint);

        http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http
                .csrf().disable().headers().frameOptions().disable();

        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .mvcMatchers("/h2-console/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass/**").hasAnyAuthority("ROLE_USER", "ROLE_ACCOUNTANT", "ROLE_ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment/**").hasAnyAuthority("ROLE_USER", "ROLE_ACCOUNTANT")
                .mvcMatchers(HttpMethod.GET, "/api/security/events/**").hasAuthority("ROLE_AUDITOR")
                .mvcMatchers(HttpMethod.POST, "/api/acct/payments/**").hasAuthority("ROLE_ACCOUNTANT")
                .mvcMatchers(HttpMethod.PUT, "/api/acct/payments/**").hasAuthority("ROLE_ACCOUNTANT")
                .mvcMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasAuthority("ROLE_ADMINISTRATOR")
                .mvcMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasAuthority("ROLE_ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/api/admin/user/**").hasAuthority("ROLE_ADMINISTRATOR")
                .mvcMatchers(HttpMethod.PUT, "/api/admin/user/access/**").hasAuthority("ROLE_ADMINISTRATOR");

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
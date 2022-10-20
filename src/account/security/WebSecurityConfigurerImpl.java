package account.security;

import account.exceptions.AccessDeniedHandlerImpl;
import account.exceptions.AuthenticationEntryPointImpl;
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
                .httpBasic();

        http
                .csrf().disable();

        http
                .headers().frameOptions().disable();

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        http

                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "api/auth/signup", "/**").permitAll();
//                .mvcMatchers("api/auth/changepass").hasAnyAuthority(ROLE_USER.name(), ROLE_ACCOUNTANT.name(), ROLE_ADMINISTRATOR.name())
//                .mvcMatchers("api/empl/payment").hasAnyAuthority(ROLE_USER.name(), ROLE_ACCOUNTANT.name())
//                .mvcMatchers("api/acct/payments").hasAuthority(ROLE_ACCOUNTANT.name())
//                .mvcMatchers("api/admin/**").hasAuthority(ROLE_ADMINISTRATOR.name());


//        http
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "api/auth/changepass").hasAnyAuthority(ROLE_USER.name(), ROLE_ACCOUNTANT.name(), ROLE_ADMINISTRATOR.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "api/empl/payment").hasAnyAuthority(ROLE_USER.name(), ROLE_ACCOUNTANT.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "api/acct/payments").hasAnyAuthority(ROLE_ACCOUNTANT.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.PUT, "api/acct/payments").hasAnyAuthority(ROLE_ACCOUNTANT.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "api/admin/user").hasAnyAuthority(ROLE_ADMINISTRATOR.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.DELETE, "api/admin/user").hasAnyAuthority(ROLE_ADMINISTRATOR.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.PUT, "api/admin/user/role").hasAnyAuthority(ROLE_ADMINISTRATOR.name())
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "api/auth/signup", "/**").permitAll();
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
package rs.ac.uns.ftn.e2.onee2team.security.pki.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.TokenUtils;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.IUserService;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		        .headers()
		        .xssProtection()
		        .and()
		        .contentSecurityPolicy("script-src 'self'");
		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				
				.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

				.authorizeRequests().antMatchers("/api/auth/**").permitAll().antMatchers("/api/certificates/download/**").permitAll().
				and().authorizeRequests().antMatchers("/api/").permitAll()
				
				.anyRequest().authenticated().and()
				
				.cors().and()

				.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userService),
						BasicAuthenticationFilter.class);
		http.csrf().disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register", "/api/users/request-recovery");
		web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
				"/**/*.css", "/**/*.js");
		web.ignoring().antMatchers(HttpMethod.PUT, "/api/users/recovery");
	}
	
}

package com.adventureseekers.adventurewebapi.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.adventureseekers.adventurewebapi.security.AuthenticationFilter;
import com.adventureseekers.adventurewebapi.security.AuthorizationFilter;
import com.adventureseekers.adventurewebapi.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserService userService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
		authenticationFilter.setFilterProcessesUrl("/api/auth/login");
		
		http.cors().and().csrf().disable().authorizeRequests()
	        .antMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
	        .antMatchers(HttpMethod.GET, "/api/auth/confirmation/**").permitAll()
	        //.antMatchers(HttpMethod.GET, "/api/auth/resend/**").permitAll()
	        .antMatchers(HttpMethod.GET, "/api/users/checkEmail/**").permitAll()
	        .antMatchers(HttpMethod.GET, "/api/users/checkUsername/**").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .addFilter(authenticationFilter)
	        .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	/**
	 * Configuration for CORS
	 */
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowCredentials(true);
      configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
	  configuration.addAllowedHeader("*");
	  configuration.addAllowedMethod("*");
	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  source.registerCorsConfiguration("/**", configuration);
      return source;
	}
	
	// beans
    /**
     * BCrypt bean definition
     */
    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    /**
     * Authentication provider bean definition
     * @return The new authentication provider
     */
  	@Bean
  	public DaoAuthenticationProvider authenticationProvider() {
  		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
  		auth.setUserDetailsService(userService); //set the custom user details service
  		auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
  		return auth;
  	}
  	
  	@Bean
  	@Override
  	public AuthenticationManager authenticationManagerBean() throws Exception {
  		return super.authenticationManagerBean();
  	}
  	
  	@Bean
  	ForwardedHeaderFilter forwardedHeaderFilter() {
  	    return new ForwardedHeaderFilter();
  	}
}













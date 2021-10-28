package com.adventureseekers.adventurewebapi.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.adventureseekers.adventurewebapi.config.SecurityConstants;

import java.security.Key;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    /**
     * When a users attempts to login, the method gets the credentials 
     * and submits them in form of a token to a authentication manager.
     */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
				HttpServletResponse res) throws AuthenticationException {
		try {
			UserEntity theUser = new ObjectMapper().readValue(req.getInputStream(), UserEntity.class);
			String username = theUser.getUserName();
			String password = theUser.getPassword();
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
			return this.authenticationManager.authenticate(authenticationToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//String username = req.getParameter("username");
		//String password = req.getParameter("password");
		
	}
	
	/**
	 * When the user is eligible to be authorized, it receives back an access token and a refresh token.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                        Authentication auth) throws IOException, ServletException {

		User user = (User) auth.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC512(SecurityConstants.KEY.getBytes());
		String access_token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(req.getRequestURL().toString())
				.withClaim("roles", 
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
				
		String refresh_token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				.withIssuer(req.getRequestURL().toString())
				.sign(algorithm);
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		res.setContentType("application/json");
		new ObjectMapper().writeValue(res.getOutputStream(), tokens);
	}
}











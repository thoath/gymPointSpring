package br.com.gympoint.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gympoint.models.User;
import br.com.gympoint.respository.UserRepository;
import br.com.gympoint.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");
		
		String email = null;
		String jwtToken = null;
		

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			
			jwtToken = requestTokenHeader.substring(7);
			
			try {
				email = jwtTokenUtil.getEmailFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			if (jwtTokenUtil.getProviderFromToken(jwtToken)) {
				authorities.add(new SimpleGrantedAuthority("ADMIN"));
				authorities.add(new SimpleGrantedAuthority("STUDENT"));
			} else {
				authorities.add(new SimpleGrantedAuthority("STUDENT"));
			}
			
			Optional<User> user = userRepository.findById(Integer.valueOf(jwtTokenUtil.getUserIdToken(jwtToken)));
			
			if(user.isPresent()) {
				
				if (jwtTokenUtil.validateToken(jwtToken, user.get())) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					user.get(), null, authorities);
					
					usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					request.setAttribute("userId", user.get().getId());
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
		
		chain.doFilter(request, response);
	}
}

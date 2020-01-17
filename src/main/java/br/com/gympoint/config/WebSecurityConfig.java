package br.com.gympoint.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.gympoint.security.JwtAuthenticationEntryPoint;
import br.com.gympoint.security.JwtRequestFilter;

/**
 * Configuracoes de seguranca e restricoes de acesso da API
 * @author lucaskoch
 * @see https://spring.io/projects/spring-security
 * @see https://jwt.io
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private static final String ADMIN = "ADMIN";
	private static final String STUDENT = "STUDENT";
	private static final String API_PATH = "/api/v1/";

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
		
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Remove os paths do swagger das validacoes
	 */
	@Override
	public void configure(WebSecurity web) {
		
		web.ignoring().antMatchers(
							"/v2/api-docs",
							"/configuration/**",
							"/swagger-resources/**",
							"/swagger-ui.html",
							"/webjars/**"
						);
		
	}
	
	/**
	 * Configura o acesso para cada endpoint da api
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
		.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers(API_PATH.concat("login"), "/csrf")
		.permitAll()
		
		/*
		 * Rotas somente para admins
		 */
		.antMatchers(
				API_PATH.concat("users/**"), 
				API_PATH.concat("registration/**"),
				API_PATH.concat("student/**"),
				API_PATH.concat("contract/**"),
				API_PATH.concat("help/**")
		)
		.hasAuthority(ADMIN)
		.antMatchers(API_PATH.concat("/students/help/**"))
		.hasAnyAuthority(STUDENT)
		.antMatchers(HttpMethod.POST,API_PATH.concat("/students/**/checkin"))
		.hasAnyAuthority(STUDENT)
		.antMatchers(HttpMethod.GET,API_PATH.concat("/students/**/checkin"))
		.hasAnyAuthority(ADMIN)
		
		/**
		 * Rotas para usuários logados
		 */
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}
	
	
}

package br.com.gympoint.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gympoint.dto.UserCredentialsDto;
import br.com.gympoint.models.User;
import br.com.gympoint.service.UserService;
import br.com.gympoint.utils.JwtResponse;
import br.com.gympoint.utils.JwtTokenUtil;
import br.com.gympoint.utils.Response;

/**
 * Endpoint para login na api
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1/login")
public class SessionController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	/**
	 * Efetua o login, validando email e senha
	 * @param credentials credenciais do usuario
	 * @param result validacoes do objeto
	 * @return jwt token
	 */
	@PostMapping()
	public ResponseEntity<Response<JwtResponse>> store(@RequestBody @Valid UserCredentialsDto credentials, BindingResult result) {
		
		List<String> errors = new  ArrayList<>();
		
		if (result.hasErrors()) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(new Response<>(null, result
							.getAllErrors()
							.stream()
							.map(error -> error.getDefaultMessage())
							.collect(Collectors.toList())
						));
		}
		
		final Optional<User> user = userService.findByEmail(credentials.getEmail());
		
		if (!user.isPresent()) {
			errors.add("Credenciais inválidas.");
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Response<>(null, errors));
		}
		
		authenticate(user.get(), credentials.getPassword(), errors);
		
		if (errors.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
		}
		
		final String token = jwtTokenUtil.generateToken(user.get());
		
		
		return ResponseEntity.status(HttpStatus.OK).body(new Response<>(new JwtResponse(token), null));
		
	}
	
	private void authenticate(User user, String password, List<String> errors) {
		
		if (!userService.isValidPassword(password, user.getPasswordHash())) {
			errors.add("Credenciais inválidas.");
		}
	}
}

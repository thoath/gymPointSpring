package br.com.gympoint.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.gympoint.dto.UserDto;
import br.com.gympoint.models.User;
import br.com.gympoint.service.UserService;
import br.com.gympoint.utils.Response;

/**
 * Endpoints para manutencao de usuarios da academia
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Lista todos os usuarios cadastrados no sistema
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Response<Set<UserDto>>> index() {
		
		return ResponseEntity.ok().body(new Response<>(userService.index(), null));
	}
	
	/**
	 * Cadastra um usuario no sistema, esse usuario pode logar posteriormente
	 * @param user objeto a ser cadfastrado
	 * @param result resultado das validacoes dos campos
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Response<UserDto>> store(@RequestBody @Valid User user, BindingResult result) {
		
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
		
		if (userService.findByEmail(user.getEmail()).isPresent()) {
			errors.add("Conta já existe no sistema , verifique o email informnado.");
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Response<>(null, errors));
		}
		
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(userService.store(user), null));
		} catch(Exception ex) {
			errors.add("Error ao processar o cadastro, tente novamente mais tarde.");		
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, errors));
		
	}
	

}

package br.com.gympoint.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gympoint.dto.RegistrationDto;
import br.com.gympoint.models.Registration;
import br.com.gympoint.service.RegistrationService;
import br.com.gympoint.utils.Response;

/**
 * Endpoints para manutencao de contratos entre aluno e academia
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;
	
	@GetMapping
	public ResponseEntity<Response<Set<RegistrationDto>>> index() {
		
		return ResponseEntity.status(HttpStatus.OK).body(new Response<>(registrationService.index(), null));
		
	}
	
	/**
	 * Retorna o contrato de um determinado aluno
	 * @param studentId id do aluno a ser pesquisado
	 * @return contrato do aluno, se existente
	 */
	@GetMapping("/student/{id}")
	public ResponseEntity<Response<RegistrationDto>> show(@PathVariable(name = "id") Integer studentId) {
		
		List<String> messages = new ArrayList<>();
		
		Optional<RegistrationDto> registration = registrationService.findByStudent(studentId);
		
		if (registration.isPresent()) {
			
			return ResponseEntity.status(HttpStatus.OK).body(new Response<>(registration.get(), null));
		}
		
		messages.add("Não existe nenhum registro com os parâmetros informados. ");
		
		return ResponseEntity.status(HttpStatus.OK).body(new Response<>(null, messages)); 
	}
	
	/**
	 * Persiste um novo contrato entre aluno e academia
	 * @param registration Contrato firmado entre aluno e academia
	 * @param result validacaos do objeto
	 * @return contrato persistido
	 */
	@PostMapping
	public ResponseEntity<Response<RegistrationDto>> store(@RequestBody @Valid Registration registration, BindingResult result) {
		
		List<String> messages = new  ArrayList<>();
		
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
		
		Optional<RegistrationDto> registrationExist = registrationService.findByStudent(registration.getStudent().getId());
		
		if (!registrationExist.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(registrationService.store(registration), null));
		}
		
		messages.add("O usuário já possui um contrato, nesse caso atualize o já existente.");
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(null, messages));
		
	}
	
	/**
	 * Atualiza um contrato ja existente na base de dados
	 * @param registration contrato a ser atualizado
	 * @param result validacoes do objeto
	 * @return contrato atualizado
	 */
	@PutMapping
	public ResponseEntity<Response<RegistrationDto>> update(@RequestBody @Valid Registration registration, BindingResult result) {
		
		List<String> messages = new  ArrayList<>();
		
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
		
		Optional<RegistrationDto> registrationExist = registrationService.findById(registration.getId());
		
		if (registrationExist.isPresent()) {
			
			if (registrationExist.get().getStudent().getId().compareTo(registration.getStudent().getId()) > 0) {
				messages.add("Não é possível alterar o usuário de um contrato.");
				return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(null, messages));
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(registrationService.store(registration), null));
			
		}
		
		messages.add("O registro não existe na base de dados para ser atualizado.");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(null, messages));
		
	}
	
	/**
	 * Deleta um contrato existente na base de dados
	 * @param id do contrato a ser excluido
	 * @return true se excluiu ou false se nao excluiu
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<RegistrationDto>> delete(@PathVariable Integer id) {
		
		List<String> messages = new  ArrayList<>();
		
		Optional<Registration> registrationExist = registrationService.findByIdModel(id);
		
		if (registrationExist.isPresent()) {
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response<>(registrationService.delete(registrationExist.get()), null));
			
		}
		
		messages.add("O registro não existe na base de dados para ser atualizado.");
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(null, messages));
	}
	
}

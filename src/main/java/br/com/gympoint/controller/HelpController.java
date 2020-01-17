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

import br.com.gympoint.dto.HelpDto;
import br.com.gympoint.models.Help;
import br.com.gympoint.models.Student;
import br.com.gympoint.service.HelpService;
import br.com.gympoint.service.StudentService;
import br.com.gympoint.utils.Response;
import br.com.gympoint.utils.ResultAction;

/**
 * Endpoints para manutencao de cadastros de ajuda aluno/instrutor
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1")
public class HelpController {

	@Autowired
	private HelpService helpService;
	
	@Autowired
	private StudentService studentService;
	
	/**
	 * Lista todos os pedidos de ajuda dos alunos
	 * @return todos os pedidos de ajuda
	 */
	@GetMapping("/help")
	public ResponseEntity<Response<Set<HelpDto>>> index() {
		
		return ResponseEntity.status(HttpStatus.OK).body(new Response<>(helpService.index(), null));
		
	}
	/**
	 * Lista todos os pedidos de ajuda de um deterninado aluno
	 * @param id do aluno
	 * @return todos os pedidos de ajudo do aluno
	 */ 
	@GetMapping("/help/{id}")
	public ResponseEntity<Response<Set<HelpDto>>> show(@PathVariable(name = "id") Integer id) {
		
		List<String> errors = new  ArrayList<>();
		
		if (studentService.studentExist(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response<>(helpService.show(studentService.findById(id).get()), null));
		} else {
			errors.add("Aluno não existe");
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
	}
	
	/**
	 * Persiste um pedido de ajuda na base
	 * @param help Ajuda a ser persistida
	 * @param studentId id do aluno que pediu ajuda
	 * @param result validacoes do objeto
	 * @return ajuda persistida
	 */
	@PostMapping("/students/{id}/help")
	public ResponseEntity<Response<HelpDto>> store(
				@RequestBody @Valid Help help, 
				@PathVariable(name = "id") Integer studentId, 
				BindingResult result
			) {
		
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
		
		Optional<Student> student = studentService.findById(studentId);
		
		if (!student.isPresent()) {
			errors.add("Aluno com esse id não existe.");
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
		}
		
		help.setStudent(student.get());
		
		return  ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(helpService.store(help), null));
		
	}
	
	/**
	 * Atualiza um pedido de ajuda ja existente na base
	 * @param help ajuda a ser atualizada
	 * @param request da chamada ao endpoint
	 * @return ajuda atualizada
	 */
	@PutMapping("/students/help")
	public ResponseEntity<Response<HelpDto>> update(@RequestBody Help help) {
		
		List<String> errors = new  ArrayList<>();
		
		Optional<Help> helpLoaded = helpService.findById(help.getId());
		
		if (! helpLoaded.isPresent()) {
			errors.add("Esse pedido de ajuda não existe na base de dados.");
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
		}
		
		try {

			return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(helpService.update(help), null));
		
		} catch (Exception e) {
			errors.add("Não foi possível alterar o pedido de ajuda.");
		}
		
		return  ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(null, errors));
		
	}
	
	/**
	 * Delete um pedido de ajuda existente na base de dados
	 * @param id do pedido de ajuda a ser deletado
	 * @return true se deletou e false se nao deletou
	 */
	@DeleteMapping("/students/help/{id}")
	public ResponseEntity<Response<ResultAction>> delete(@PathVariable(name = "id") Integer id) {
		
		List<String> errors = new  ArrayList<>();
		Optional<Help> helpLoaded = helpService.findById(id);
		
		if (! helpLoaded.isPresent()) {
			errors.add("Esse pedido de ajuda não existe na base de dados.");
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
		}
		
		if (helpService.delete(id)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response<>(new ResultAction(true), null));
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response<>(new ResultAction(false), null));
		
	}
	
}

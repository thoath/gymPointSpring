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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gympoint.dto.StudentDto;
import br.com.gympoint.models.Student;
import br.com.gympoint.service.StudentService;
import br.com.gympoint.utils.Response;

/**
 * Endpoints para manutencao de alunos da academia
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	/**
	 * Recupera todos os alunos cadastrados na base de dados
	 * @return
	 */
	@GetMapping
	public ResponseEntity<Response<Set<StudentDto>>> index() {
		return ResponseEntity.status(HttpStatus.OK).body(new Response<>(studentService.index(), null));
	}
	
	@PostMapping
	public ResponseEntity<Response<StudentDto>> store(@RequestBody @Valid Student student, BindingResult result) {
		
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
		
		if (studentService.findByEmail(student.getEmail()).isPresent()) {
			messages.add("Aluno já existe no sistema , verifique o email informado.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(null, messages));
		}
		
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(studentService.store(student), null));
		} catch(Exception e) {
			messages.add("Error ao processar o cadastro, tente novamente mais tarde.");
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, messages));
	}
	
	@PutMapping
	public ResponseEntity<Response<StudentDto>> update(@RequestBody @Valid Student student, BindingResult result) {
		
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
		
		Optional<Student> studentDb = studentService.findById(student.getId());
		
		if (studentDb.isPresent()) {
			
			if (!studentDb.get().getEmail().equalsIgnoreCase(student.getEmail()) 
					&& studentService.findByEmail(student.getEmail()).isPresent()) {
				
				messages.add("Aluno já existe no sistema , verifique o email informado.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(null, messages));
			}
			
			try {
				return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(studentService.update(student), null));
			} catch(Exception e) {
				messages.add("Error ao processar o cadastro, tente novamente mais tarde.");
			}
		}
	
		messages.add("O aluno informado não existe na base de dados, verifique os dados informados.");
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, messages));
	}
	
}

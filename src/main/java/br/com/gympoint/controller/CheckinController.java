package br.com.gympoint.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gympoint.dto.CheckinDto;
import br.com.gympoint.schemas.Checkins;
import br.com.gympoint.service.CheckinService;
import br.com.gympoint.service.StudentService;
import br.com.gympoint.utils.Response;

/**
 * Endpoints para manutencao de checkins de alunos da academia
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1/students")
public class CheckinController {
	
	@Autowired
	private CheckinService checkinService;
	
	@Autowired
	private StudentService studentService;
	
	/**
	 * Lista todos os checkins de um aluno de acordo com seu id
	 * @param studentId id do aluno
	 * @return lista de checkins feitas pelo aluno
	 */
	@GetMapping("/{id}/checkin")
	public ResponseEntity<Response<List<CheckinDto>>> index(@PathVariable(name = "id") Integer studentId){
		
		List<String> errors = new  ArrayList<>();
		
		if (studentService.studentExist(studentId)) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response<>(checkinService.show(studentId), null));
		} else {
			errors.add("Aluno não existe");
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
		
	}
	
	/**
	 * Grava um checkin de um determinado aluno ativo no sistema
	 * @param id do aluno
	 * @return checkin criado
	 */
	@PostMapping("/{id}/checkin")
	public ResponseEntity<Response<CheckinDto>> store(@PathVariable(name = "id") Integer id) {
		
		List<String> errors = new  ArrayList<>();
		
		if (studentService.studentExist(id)) {
			
			Checkins checkin = new Checkins(id);
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(checkinService.store(checkin), null));
		
		} else {
			errors.add("Aluno não existe");
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, errors));
		
	}
	
}

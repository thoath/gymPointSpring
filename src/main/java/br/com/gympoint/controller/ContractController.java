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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gympoint.dto.ContractDto;
import br.com.gympoint.models.Contract;
import br.com.gympoint.service.ContractService;
import br.com.gympoint.utils.Response;
import br.com.gympoint.utils.ResultAction;

/**
 * Endpoints para manutencao de planos da academia
 * @author lucaskoch
 *
 */
@RestController
@RequestMapping("/api/v1/contract")
public class ContractController {

	@Autowired
	private ContractService contractService;
	
	@GetMapping
	public ResponseEntity<Response<Set<ContractDto>>> index(){
		return ResponseEntity.status(HttpStatus.OK).body(new Response<>(contractService.index(), null));
	}
	
	/**
	 * Persiste um novo plano da academia
	 * @param contract plano a ser inserido na base
	 * @param result validacaos do objeto
	 * @return contrato persistido
	 */
	@PostMapping
	public ResponseEntity<Response<ContractDto>> store(@RequestBody @Valid Contract contract, BindingResult result) {
	
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
		
		ContractDto contractDao = contractService.findByTitle(contract.getTitle());
		
		if (contractDao != null) {
			messages.add("Contrato já existe com esse titulo.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, messages));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(contractService.store(contract), null));
		
	}
	
	/**
	 * Deleta um plano da base de dados
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Response<ResultAction>> delete(@PathVariable(name = "id") Integer id) {
		
		Optional<Contract> contract = contractService.findById(id);
		List<String> errors = new  ArrayList<>();
		Boolean success = false;
		
		if (contract.isPresent()) {
			success = contractService.delete(id);
			errors = null; 
		} else {
			errors.add("Contrato não existe");
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response<>(new ResultAction(success), errors));
				
	}
	
}

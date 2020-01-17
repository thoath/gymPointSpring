package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;

import br.com.gympoint.dto.ContractDto;
import br.com.gympoint.models.Contract;

public interface ContractService {

	Set<ContractDto> index();
	ContractDto store(Contract contract);
	ContractDto update(Contract contract);
	Boolean delete(Integer contractId);
	ContractDto findByTitle(String title);
	Optional<Contract> findById(Integer id);
}
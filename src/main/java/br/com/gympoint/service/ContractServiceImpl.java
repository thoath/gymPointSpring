package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gympoint.dto.ContractDto;
import br.com.gympoint.models.Contract;
import br.com.gympoint.respository.ContractRepository;

@Service
public class ContractServiceImpl implements ContractService{

	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Set<ContractDto> index() {
		return contractRepository
				.findAll()
				.stream()
				.map(contract -> modelMapper.map(contract, ContractDto.class))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	@Override
	public ContractDto store(Contract contract) {
		return saveContract(contract);
	}

	@Override
	public ContractDto update(Contract contract) {
		return saveContract(contract);
	}

	@Override
	public Boolean delete(Integer contractId) {
		
		try {
			contractRepository.deleteById(contractId);
		} catch(Exception e) {
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	private ContractDto saveContract(Contract contract) {
		return modelMapper.map(contractRepository.save(contract), ContractDto.class);
	}

	@Override
	public ContractDto findByTitle(String title) {
		
		Optional<Contract> contract = contractRepository.findByTitle(title);
		
		if (contract.isPresent()) {
			return modelMapper.map(contractRepository.findByTitle(title).get(), ContractDto.class);
		}
		
		return null;
		
	}

	@Override
	public Optional<Contract> findById(Integer id) {
		return contractRepository.findById(id);
	}

}

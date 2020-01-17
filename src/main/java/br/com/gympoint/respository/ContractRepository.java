package br.com.gympoint.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gympoint.models.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer>{
	Optional<Contract> findByTitle(String title);
}

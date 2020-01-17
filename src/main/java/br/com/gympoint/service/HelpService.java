package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;

import br.com.gympoint.dto.HelpDto;
import br.com.gympoint.models.Help;
import br.com.gympoint.models.Student;

public interface HelpService {

	Set<HelpDto> index();
	Set<HelpDto> show(Student student);
	HelpDto store(Help help);
	HelpDto update(Help help);
	boolean delete(Integer helpId);
	Optional<Help> findById(Integer id);
	boolean helpExist(Integer id);
	
}

package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;

import br.com.gympoint.dto.RegistrationDto;
import br.com.gympoint.models.Registration;

public interface RegistrationService {

	Optional<RegistrationDto> findByStudent(Integer studentId);
	Optional<RegistrationDto> findById(Integer id);
	Optional<Registration> findByIdModel(Integer id);
	Set<RegistrationDto> index();
	RegistrationDto store(Registration registration);
	RegistrationDto update(Registration registration);
	RegistrationDto delete(Registration registration);
}

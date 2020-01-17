package br.com.gympoint.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gympoint.models.Registration;
import br.com.gympoint.models.Student;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer>{

	Optional<Registration> findByStudent(Student student);
}

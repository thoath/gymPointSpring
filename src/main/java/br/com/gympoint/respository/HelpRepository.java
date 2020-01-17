package br.com.gympoint.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gympoint.models.Help;
import br.com.gympoint.models.Student;

@Repository
public interface HelpRepository extends JpaRepository<Help, Integer>{
	
	 Optional<List<Help>> findByStudent(Student student);
	
}

package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;

import br.com.gympoint.dto.StudentDto;
import br.com.gympoint.models.Student;

public interface StudentService {
	
	Set<StudentDto> index();
	StudentDto store(Student student);
	StudentDto update(Student student);
	boolean studentExist(Integer id);
	Optional<Student> findByEmail(String email);
	Optional<Student> findById(Integer id);

}

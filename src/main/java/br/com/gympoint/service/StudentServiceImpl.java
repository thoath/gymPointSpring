package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gympoint.dto.StudentDto;
import br.com.gympoint.models.Student;
import br.com.gympoint.respository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{

	@Autowired
	private StudentRepository studentrepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Set<StudentDto> index() {
		
		return studentrepository
				.findAll()
				.stream()
				.map(student -> modelMapper.map(student, StudentDto.class))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	@Override
	public StudentDto store(Student student) {
		return saveStudent(student);
	}

	@Override
	public StudentDto update(Student student) {
		return saveStudent(student);
	}
	
	private StudentDto saveStudent(Student student) {
		return modelMapper.map(studentrepository.save(student),StudentDto.class);
	}

	@Override
	public boolean studentExist(Integer id) {
		return this.findById(id).isPresent();
	}

	@Override
	public Optional<Student> findByEmail(String email) {
		return studentrepository.findByEmail(email);
	}

	public Optional<Student> findById(Integer id) {
		return studentrepository.findById(id);
	}

}

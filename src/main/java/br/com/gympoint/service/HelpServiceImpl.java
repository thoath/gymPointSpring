package br.com.gympoint.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gympoint.dto.HelpDto;
import br.com.gympoint.models.Help;
import br.com.gympoint.models.Student;
import br.com.gympoint.respository.HelpRepository;

@Service
public class HelpServiceImpl implements HelpService{

	@Autowired
	private HelpRepository helpRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Set<HelpDto> index() {
		return helpRepository.
				findAll()
				.stream()
				.map(help -> modelMapper.map(help, HelpDto.class))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	@Override
	public Set<HelpDto> show(Student student) {
		
		Optional<List<Help>> helps = helpRepository.findByStudent(student);
		
		if(helps.isPresent()) {
			
			return helps.get()
					.stream()
					.map(help -> modelMapper.map(help, HelpDto.class))
					.collect(Collectors.toCollection(TreeSet::new));
		}

		return new TreeSet<>();
	}

	@Override
	public HelpDto store(Help help) {
		return saveHelp(help);
	}

	@Override
	public HelpDto update(Help help) {
		return saveHelp(help);
	}

	@Override
	public boolean helpExist(Integer id) {
		return helpRepository.findById(id).isPresent();
	}
	
	private HelpDto saveHelp(Help help) {
		return modelMapper.map(helpRepository.save(help), HelpDto.class);
	}

	@Override
	public Optional<Help> findById(Integer id) {
		return helpRepository.findById(id);
	}

	@Override
	public boolean delete(Integer helpId) {
		
		try {
			helpRepository.deleteById(helpId);
		} catch (IllegalArgumentException e) {
			return false;
		}
		
		return true;
	}

}

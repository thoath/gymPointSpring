package br.com.gympoint.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gympoint.dto.CheckinDto;
import br.com.gympoint.respository.CheckinRepository;
import br.com.gympoint.schemas.Checkins;

@Service
public class CheckinServiceImpl implements CheckinService{

	@Autowired
	private CheckinRepository checkinRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CheckinDto store(Checkins checkin) {
		return modelMapper.map(checkinRepository.save(checkin), CheckinDto.class);
	}

	@Override
	public List<CheckinDto> show(Integer studentId) {
		
		return checkinRepository
				.findByStudent(studentId)
				.stream()
				.map(checkin -> modelMapper.map(checkin, CheckinDto.class))
				.collect(Collectors.toCollection(ArrayList::new));
		
	}

}

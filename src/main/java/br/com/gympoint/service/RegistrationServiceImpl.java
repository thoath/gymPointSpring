package br.com.gympoint.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gympoint.dto.ContractDto;
import br.com.gympoint.dto.RegistrationDto;
import br.com.gympoint.dto.StudentDto;
import br.com.gympoint.models.Contract;
import br.com.gympoint.models.Registration;
import br.com.gympoint.models.Student;
import br.com.gympoint.respository.ContractRepository;
import br.com.gympoint.respository.RegistrationRepository;
import br.com.gympoint.respository.StudentRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService{

	@Autowired
	private RegistrationRepository registrationRespository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
    private Queue queue;
	
	@Override
	public Optional<RegistrationDto> findByStudent(Integer studentId) {
		
		Optional<Student> student = studentRepository.findById(studentId);
		
		if (!student.isPresent()) {
			return Optional.empty();
		}
		
		Optional<Registration> registration = registrationRespository.findByStudent(student.get());
		
		if (registration.isPresent()) {
			return Optional.of(modelMapper.map(registration.get(), RegistrationDto.class));
		}

		return Optional.empty();
		
	}

	@Override
	public Set<RegistrationDto> index() {
		return registrationRespository
				.findAll()
				.stream()
				.map(registration -> modelMapper.map(registration, RegistrationDto.class))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	@Override
	public RegistrationDto store(Registration registration) {
		
		RegistrationDto registrationDto = this.save(registration);

		this.updateRegistrationDto(registrationDto);
		
		try {
			this.sendRegistrationMessage(registrationDto);
		} catch(AmqpException e) {
			System.out.println("Não foi possível enviar a mensagem para o envio de email.");
		}
		
		return registrationDto;
		
	}

	@Override
	public RegistrationDto update(Registration registration) {
		return this.save(registration);
	}
	
	private RegistrationDto save(Registration registration) {
		
		Contract contract = contractRepository.findById(registration.getContract().getId()).get();
		
		LocalDateTime finalDate = registration.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		finalDate = finalDate.plusMonths(contract.getDuration());
		
		BigDecimal bd = new BigDecimal(contract.getPrice() * contract.getDuration());
		
		registration.setEndDate(Timestamp.valueOf(finalDate));
		registration.setPrice(bd);
		
		return modelMapper.map(registrationRespository.save(registration), RegistrationDto.class);
	}

	@Override
	public Optional<RegistrationDto> findById(Integer id) {
		
		Optional<Registration> registration = registrationRespository.findById(id);
		
		if (registration.isPresent()) {
			return Optional.of(modelMapper.map(registrationRespository.findById(id).get(), RegistrationDto.class)) ;
		}
		
		return Optional.empty();
	}

	@Override
	public RegistrationDto delete(Registration registration) {
		
		registration.setCanceledAt(new Date());
		
		RegistrationDto registrationDto = this.save(registration);

		this.updateRegistrationDto(registrationDto);
		
		this.sendRegistrationMessage(registrationDto);
		
		return registrationDto;
	}

	@Override
	public Optional<Registration> findByIdModel(Integer id) {
		return registrationRespository.findById(id);
	}
	
	private void updateRegistrationDto(RegistrationDto registration){
		
		Student student = studentRepository.findById(registration.getStudent().getId()).get();
		Contract contract = contractRepository.findById(registration.getContract().getId()).get();
		
		registration.setContract(modelMapper.map(contract, ContractDto.class));
		registration.setStudent(modelMapper.map(student, StudentDto.class));
	}
	
	private void sendRegistrationMessage(RegistrationDto registration) {
		rabbitTemplate.convertAndSend(this.queue.getName(), registration);
	}
	

}

package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gympoint.dto.UserDto;
import br.com.gympoint.models.User;
import br.com.gympoint.respository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Set<UserDto> index() {
		
		return userRepository
				.findAll()
				.stream()
				.map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	@Override
	public UserDto store(User user) {
		user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
		return modelMapper.map(userRepository.save(user), UserDto.class);
	}

	@Override
	public boolean isValidPassword(String password, String passWordEncoded) {
		return passwordEncoder.matches(password, passWordEncoded);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}

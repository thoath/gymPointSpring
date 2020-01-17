package br.com.gympoint.service;

import java.util.Optional;
import java.util.Set;

import br.com.gympoint.dto.UserDto;
import br.com.gympoint.models.User;

public interface UserService {
	
	Set<UserDto> index();
	UserDto store(User user);
	boolean isValidPassword(String password, String passWordEncoded);
	Optional<User> findByEmail(String email);
}

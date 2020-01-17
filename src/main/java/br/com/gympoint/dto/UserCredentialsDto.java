package br.com.gympoint.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserCredentialsDto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Email(message = "Email inválido.")
	private String email;
	@NotEmpty(message = "Senha precisa ser preenchida.")
	private String password;
	
	public UserCredentialsDto() {}
	
	public UserCredentialsDto(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

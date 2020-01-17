package br.com.gympoint.utils;

import java.io.Serializable;

public class JwtResponse implements Serializable{

	private static final long serialVersionUID = 5655896000850267406L;
	private final String token;
	

	public JwtResponse(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return this.token;
	}
	
}

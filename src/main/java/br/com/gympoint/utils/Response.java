package br.com.gympoint.utils;

import java.io.Serializable;
import java.util.List;

public class Response<T> implements Serializable{
	
	private static final long serialVersionUID = 1016727337709583157L;

	private T data;
	
	private List<String> messages;
	
	public Response() {}

	public Response(T data, List<String> messages) {
		super();
		this.data = data;
		this.messages = messages;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the error
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * @param error the error to set
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	

}

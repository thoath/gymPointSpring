package br.com.gympoint.utils;

public class ResultAction {

	private Boolean success;

	public ResultAction() {}
	
	public ResultAction(Boolean success) {
		super();
		this.success = success;
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	
	
}

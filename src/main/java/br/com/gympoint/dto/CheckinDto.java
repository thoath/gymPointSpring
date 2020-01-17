package br.com.gympoint.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckinDto implements Comparable<CheckinDto>{

	private Integer student;
	private Date createdAt;
	
	public CheckinDto() {}
	
	
	public CheckinDto(Integer student, Date createdAt) {
		super();
		this.student= student;
		this.createdAt = createdAt;
	}

	/**
	 * @return the studentId
	 */
	public Integer getStudent() {
		return student;
	}
	/**
	 * @param studentthe studentto set
	 */
	public void setStudent(Integer student) {
		this.student= student;
	}
	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	@Override
	public int compareTo(CheckinDto o) {
		return this.getStudent().compareTo(o.getStudent());
	}
	
	
	
}

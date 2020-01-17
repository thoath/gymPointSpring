package br.com.gympoint.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationDto implements Comparable<RegistrationDto>, Serializable{

	private static final long serialVersionUID = 8552980240139055125L;

	private Integer id;
	private Date startDate;
	private Date endDate;
	private Date canceledAt;
	private BigDecimal price;
	private StudentDto student;
	private ContractDto contract;
	
	public RegistrationDto() {}
	
	public RegistrationDto(Date startDate, Date endDate, Date canceledAt, BigDecimal price, StudentDto student,
			ContractDto contract, Integer id) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.canceledAt = canceledAt;
		this.price = price;
		this.student = student;
		this.contract = contract;
		this.id = id;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the canceledAt
	 */
	public Date getCanceledAt() {
		return canceledAt;
	}
	/**
	 * @param canceledAt the canceledAt to set
	 */
	public void setCanceledAt(Date canceledAt) {
		this.canceledAt = canceledAt;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the student
	 */
	public StudentDto getStudent() {
		return student;
	}

	/**
	 * @param student the student to set
	 */
	public void setStudent(StudentDto student) {
		this.student = student;
	}

	/**
	 * @return the contract
	 */
	public ContractDto getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(ContractDto contract) {
		this.contract = contract;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int compareTo(RegistrationDto o) {
		return this.student.getName().compareTo(o.getStudent().getName());
	}
	
}

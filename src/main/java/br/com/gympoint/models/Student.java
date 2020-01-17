package br.com.gympoint.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "students")
public class Student extends AuditModel{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "O nome precisa ser preenchido")
	private String name;
	
	@NotEmpty(message="Email não pode estar vazio.")
	@Email(message = "Deve ser informado um email válido.")
	@Column(unique=true)
	private String email;
	
	@Min(0)
	@Max(100)
	@NotNull(message = "A idade precisa ser preenchida.")
	private Integer age;
	
	@Min(0)
	@NotNull(message = "O peso precisa ser preenchido.")
	private Double weight;
	
	@Min(0)
	@NotNull(message = "A altura precisa ser preenchida.")
	private Double height;
	
	private Boolean active = new Boolean(true);
	
	@OneToMany(mappedBy = "student")
	private List<Help> helps;
	
	@OneToOne(mappedBy = "student")
	private Registration registration;

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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/**
	 * @return the height
	 */
	public Double getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(Double height) {
		this.height = height;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the helps
	 */
	public List<Help> getHelps() {
		return helps;
	}

	/**
	 * @param helps the helps to set
	 */
	public void setHelps(List<Help> helps) {
		this.helps = helps;
	}

	/**
	 * @return the registration
	 */
	public Registration getRegistration() {
		return registration;
	}

	/**
	 * @param registration the registration to set
	 */
	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
	
}

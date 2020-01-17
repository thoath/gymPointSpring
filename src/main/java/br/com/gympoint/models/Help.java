package br.com.gympoint.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
public class Help extends AuditModel {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "A pergunta precisa ser preenchida.")
	private String question;
	
	private String answer;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date answerAt;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;
	
	@PreUpdate
	private void preUpdate() {
		
		if (this.answer != null && this.answerAt == null) {
			this.answerAt = new Date();
		}
		
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

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return the answerAt
	 */
	public Date getAnswerAt() {
		return answerAt;
	}

	/**
	 * @param answerAt the answerAt to set
	 */
	public void setAnswerAt(Date answerAt) {
		this.answerAt = answerAt;
	}

	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}
	
}

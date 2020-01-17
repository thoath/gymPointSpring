package br.com.gympoint.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpDto implements Comparable<HelpDto>{

	
	private Integer id;
	private String question;
	private String answer;
	private Date answerAt;
	private StudentDto student;
	
	public HelpDto() {}
	
	public HelpDto(Integer id, String question, String answer, Date answerAt, StudentDto student) {
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.answerAt = answerAt;
		this.student = student;
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
	public StudentDto getStudent() {
		return student;
	}

	/**
	 * @param student the student to set
	 */
	public void setStudent(StudentDto student) {
		this.student = student;
	}

	@Override
	public int compareTo(HelpDto o) {
		return this.question.compareTo(o.question);
	}
	
}

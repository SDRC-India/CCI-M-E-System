package org.sdrc.scps.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class QuestionOptionTypeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_option_type_mappingId_pk")
    private int questionOptionTypeMappingId;


    @ManyToOne
    @JoinColumn(name="option_type_fk")
    private OptionType optionType;


    @OneToOne
    @JoinColumn(name="question_id_fk")
    private Question question;


	public int getQuestionOptionTypeMappingId() {
		return questionOptionTypeMappingId;
	}


	public void setQuestionOptionTypeMappingId(int questionOptionTypeMappingId) {
		this.questionOptionTypeMappingId = questionOptionTypeMappingId;
	}


	public OptionType getOptionType() {
		return optionType;
	}


	public void setOptionType(OptionType optionType) {
		this.optionType = optionType;
	}


	public Question getQuestion() {
		return question;
	}


	public void setQuestion(Question question) {
		this.question = question;
	}

}
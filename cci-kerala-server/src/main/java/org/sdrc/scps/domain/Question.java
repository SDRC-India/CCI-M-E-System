package org.sdrc.scps.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Harsh Pratyush
 * 
 */
@Entity
@Table(name = "mst_questions")
public class Question {

    @Id
    @Column(name = "question_id_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    @Column(nullable = false)
    private String questionNameId;

    @Column(length = 700)
    private String questionName;

    @Column(nullable = false)
    private int questionOrder;

    @Column(nullable = false)
    private String columnn;

    @Column(nullable = false)
    private boolean isLive;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private String updatedBy;

    @Column(nullable = false)
    private String controlType;

    @Column(nullable = false)
    private String inputType;

    @ManyToOne
    @JoinColumn(name = "section_id_fk", nullable = false)
    private Sections section;

    @ManyToOne
    @JoinColumn(name = "form_id_fk", nullable = false)
    private Forms form;

    @Column(nullable = false)
    private boolean reviewHeader;

    private String reviewName;

    @Column(name = "dependecy", nullable = false)
    private Boolean dependecy;

   @Column(name = "dependent_column")

    private String dependentColumn;

    @Column(name = "dependent_condition", length = 700)
    private String dependentCondition;

    @OneToOne(mappedBy = "question")
    private QuestionOptionTypeMapping questionOptionTypeMapping;
    
    
    private String fileExtensions;
    
    
    private String constraints;
    
    @Column(nullable=false)
	private boolean saveMandatory ;

    @Column(nullable=false)
	private boolean finalizeMandatory ;
    
    @Column(nullable=false)
    private boolean approvalProcess;
    
    
    private String groupId;
    
    
    @Column(nullable=false)
    private boolean isTriggable;
    
    
    @Column(length=700)
    private String features;
    
    @Column(length=700)
    private String defaultSetting;
    
    @Column(length=700)
    private String placeHolder;
    
    
    private String constraintString;
    
    
    public String getPlaceHolder() {
		return placeHolder;
	}


	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}


	private String questionSerial;


	public int getQuestionId() {
		return questionId;
	}


	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}


	public String getQuestionNameId() {
		return questionNameId;
	}


	public void setQuestionNameId(String questionNameId) {
		this.questionNameId = questionNameId;
	}


	public String getQuestionName() {
		return questionName;
	}


	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}


	public int getQuestionOrder() {
		return questionOrder;
	}


	public void setQuestionOrder(int questionOrder) {
		this.questionOrder = questionOrder;
	}


	public String getColumnn() {
		return columnn;
	}


	public void setColumnn(String columnn) {
		this.columnn = columnn;
	}


	public boolean isLive() {
		return isLive;
	}


	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getControlType() {
		return controlType;
	}


	public void setControlType(String controlType) {
		this.controlType = controlType;
	}


	public String getInputType() {
		return inputType;
	}


	public void setInputType(String inputType) {
		this.inputType = inputType;
	}


	public Sections getSection() {
		return section;
	}


	public void setSection(Sections section) {
		this.section = section;
	}


	public Forms getForm() {
		return form;
	}


	public void setForm(Forms form) {
		this.form = form;
	}


	public boolean isReviewHeader() {
		return reviewHeader;
	}


	public void setReviewHeader(boolean reviewHeader) {
		this.reviewHeader = reviewHeader;
	}


	public String getReviewName() {
		return reviewName;
	}


	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}


	public Boolean getDependecy() {
		return dependecy;
	}


	public void setDependecy(Boolean dependecy) {
		this.dependecy = dependecy;
	}


	public String getDependentColumn() {
		return dependentColumn;
	}


	public void setDependentColumn(String dependentColumn) {
		this.dependentColumn = dependentColumn;
	}


	public String getDependentCondition() {
		return dependentCondition;
	}


	public void setDependentCondition(String dependentCondition) {
		this.dependentCondition = dependentCondition;
	}


	public QuestionOptionTypeMapping getQuestionOptionTypeMapping() {
		return questionOptionTypeMapping;
	}


	public void setQuestionOptionTypeMapping(QuestionOptionTypeMapping questionOptionTypeMapping) {
		this.questionOptionTypeMapping = questionOptionTypeMapping;
	}


	public String getFileExtensions() {
		return fileExtensions;
	}


	public void setFileExtensions(String fileExtensions) {
		this.fileExtensions = fileExtensions;
	}


	public String getConstraints() {
		return constraints;
	}


	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}


	public boolean isSaveMandatory() {
		return saveMandatory;
	}


	public void setSaveMandatory(boolean saveMandatory) {
		this.saveMandatory = saveMandatory;
	}


	public boolean isFinalizeMandatory() {
		return finalizeMandatory;
	}


	public void setFinalizeMandatory(boolean finalizeMandatory) {
		this.finalizeMandatory = finalizeMandatory;
	}


	public boolean isApprovalProcess() {
		return approvalProcess;
	}


	public void setApprovalProcess(boolean approvalProcess) {
		this.approvalProcess = approvalProcess;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public boolean isTriggable() {
		return isTriggable;
	}


	public void setTriggable(boolean isTriggable) {
		this.isTriggable = isTriggable;
	}


	public String getFeatures() {
		return features;
	}


	public void setFeatures(String features) {
		this.features = features;
	}


	public String getDefaultSetting() {
		return defaultSetting;
	}


	public void setDefaultSetting(String defaultSetting) {
		this.defaultSetting = defaultSetting;
	}


	public String getConstraintString() {
		return constraintString;
	}


	public void setConstraintString(String constraintString) {
		this.constraintString = constraintString;
	}


	public String getQuestionSerial() {
		return questionSerial;
	}


	public void setQuestionSerial(String questionSerial) {
		this.questionSerial = questionSerial;
	}

	
}

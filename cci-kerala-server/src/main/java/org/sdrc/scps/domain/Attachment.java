/**
 * 
 */
package org.sdrc.scps.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Entity
public class Attachment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attachment_id")
	private Long attachmentId;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "original_Name")
	private String originalName;
	
	@Column(name="column_name")
	private String columnName;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="inmate_submission_id_fk")
	private InmatesData inmateData;
	
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="institution_submission_id_fk")
	private InstitutionData institutionData;

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public InmatesData getInmateData() {
		return inmateData;
	}

	public void setInmateData(InmatesData inmateData) {
		this.inmateData = inmateData;
	}

	public InstitutionData getInstitutionData() {
		return institutionData;
	}

	public void setInstitutionData(InstitutionData institutionData) {
		this.institutionData = institutionData;
	}
	
	

}

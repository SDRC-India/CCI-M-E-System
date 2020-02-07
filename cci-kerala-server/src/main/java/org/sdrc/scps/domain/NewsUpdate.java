/**
 * 
 */
package org.sdrc.scps.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Harsh Pratyush
 * 
 *
 */

@Entity
@Table(name="news_update")
public class NewsUpdate {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int newsUpdateId;
	
	@Column(length=900)
	private String newsTitle;
	
	@Column(length=900)
	private String newsUrl;
	
	@JsonIgnore
	@CreationTimestamp
	@JsonFormat(pattern="dd/MM/YYYY")
	@Column(nullable = false, updatable = false)
	private Date createdDate;

	@JsonIgnore
	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_account_fk")
	private Account createdBy;

	@JsonIgnore
	@UpdateTimestamp
	@JsonFormat(pattern="dd/MM/YYYY")
	private Date updatedDate;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "updated_account_fk")
	private Account updatedBy;
	
	
	private boolean isLive;

	/**
	 * @return the newsUpdateId
	 */
	public int getNewsUpdateId() {
		return newsUpdateId;
	}

	/**
	 * @return the newsTitle
	 */
	public String getNewsTitle() {
		return newsTitle;
	}

	/**
	 * @return the newsUrl
	 */
	public String getNewsUrl() {
		return newsUrl;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the createdBy
	 */
	public Account getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @return the updatedBy
	 */
	public Account getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param newsUpdateId the newsUpdateId to set
	 */
	public void setNewsUpdateId(int newsUpdateId) {
		this.newsUpdateId = newsUpdateId;
	}

	/**
	 * @param newsTitle the newsTitle to set
	 */
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	/**
	 * @param newsUrl the newsUrl to set
	 */
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Account createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Account updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}

	/**
	 * @param isLive the isLive to set
	 */
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	
	
	
}

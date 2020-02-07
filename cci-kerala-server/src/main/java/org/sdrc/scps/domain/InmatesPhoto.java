/**
 * 
 */
package org.sdrc.scps.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Entity
public class InmatesPhoto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int inmatesPhotoId;

	@ManyToOne
	@JoinColumn(name = "submission_id_fk")
	private InmatesData inmatesData;

	private String photoQ1;
	
	@Temporal(TemporalType.DATE)
	private Date photoQ2;
	
	private String photoQ3;

	private boolean isLive;

	private int indexTrackNum;

	/**
	 * @return the inmatesPhotoId
	 */
	public int getInmatesPhotoId() {
		return inmatesPhotoId;
	}

	/**
	 * @return the inmatesData
	 */
	public InmatesData getInmatesData() {
		return inmatesData;
	}

	/**
	 * @return the isLive
	 */
	public boolean isLive() {
		return isLive;
	}

	/**
	 * @return the indexTrackNum
	 */
	public int getIndexTrackNum() {
		return indexTrackNum;
	}

	/**
	 * @param inmatesPhotoId the inmatesPhotoId to set
	 */
	public void setInmatesPhotoId(int inmatesPhotoId) {
		this.inmatesPhotoId = inmatesPhotoId;
	}

	/**
	 * @param inmatesData the inmatesData to set
	 */
	public void setInmatesData(InmatesData inmatesData) {
		this.inmatesData = inmatesData;
	}

	/**
	 * @param isLive the isLive to set
	 */
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	/**
	 * @param indexTrackNum the indexTrackNum to set
	 */
	public void setIndexTrackNum(int indexTrackNum) {
		this.indexTrackNum = indexTrackNum;
	}

	/**
	 * @return the photoQ1
	 */
	public String getPhotoQ1() {
		return photoQ1;
	}

	/**
	 * @return the photoQ2
	 */
	public Date getPhotoQ2() {
		return photoQ2;
	}

	/**
	 * @return the photoQ3
	 */
	public String getPhotoQ3() {
		return photoQ3;
	}

	/**
	 * @param photoQ1 the photoQ1 to set
	 */
	public void setPhotoQ1(String photoQ1) {
		this.photoQ1 = photoQ1;
	}

	/**
	 * @param photoQ2 the photoQ2 to set
	 */
	public void setPhotoQ2(Date photoQ2) {
		this.photoQ2 = photoQ2;
	}

	/**
	 * @param photoQ3 the photoQ3 to set
	 */
	public void setPhotoQ3(String photoQ3) {
		this.photoQ3 = photoQ3;
	}
	
	

}

/**
 * 
 */
package org.sdrc.scps.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */

@Entity
@Table(name="mst_inmatesid_generation")
public class InmatesIdGeneration {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int inmateIdGenerationId;
	
	
	private String lastId;
	

	@OneToOne
	@JoinColumn(name="cci_id_fk")
	private Area area;


	public int getInmateIdGenerationId() {
		return inmateIdGenerationId;
	}


	public void setInmateIdGenerationId(int inmateIdGenerationId) {
		this.inmateIdGenerationId = inmateIdGenerationId;
	}


	public String getLastId() {
		return lastId;
	}


	public void setLastId(String lastId) {
		this.lastId = lastId;
	}


	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}
	
	
	
	
}

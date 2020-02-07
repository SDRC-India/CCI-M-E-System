/**
 * 
 */
package org.sdrc.scps.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */


@Entity
@Table(name="mst_area_level")
public class AreaLevel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="area_level_id_pk")
	private int areaLevelId;
	
	
	@Column(nullable=false)
	private String areaLevelName;
	
	
	@Column(nullable=false,name="order_level")
	private int order;
	
	
	@Column(nullable=false)
	private boolean isLive;


	public AreaLevel(int areaLevelId) {
		super();
		this.areaLevelId = areaLevelId;
	}


	public AreaLevel() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getAreaLevelId() {
		return areaLevelId;
	}


	public void setAreaLevelId(int areaLevelId) {
		this.areaLevelId = areaLevelId;
	}


	public String getAreaLevelName() {
		return areaLevelName;
	}


	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public boolean isLive() {
		return isLive;
	}


	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
	 

}

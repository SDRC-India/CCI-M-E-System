/**
 * 
 */
package org.sdrc.scps.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */


@Entity
@Table(name="mst_option_type")
public class OptionType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="option_type_id_pk")
	private int optionTypeId;
	
	
	@Column(nullable=false)
	private String optionTypeName;
	
	
	@Column(nullable=false)
	private boolean isLive;
	
	@OneToMany(mappedBy="optionType")
	List<Options> options;


	public int getOptionTypeId() {
		return optionTypeId;
	}


	public void setOptionTypeId(int optionTypeId) {
		this.optionTypeId = optionTypeId;
	}


	public String getOptionTypeName() {
		return optionTypeName;
	}


	public void setOptionTypeName(String optionTypeName) {
		this.optionTypeName = optionTypeName;
	}


	public boolean isLive() {
		return isLive;
	}


	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}


	public List<Options> getOptions() {
		return options;
	}


	public void setOptions(List<Options> options) {
		this.options = options;
	}
	
	

}

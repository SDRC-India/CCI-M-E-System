package org.sdrc.scps.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Harsh Pratyush
 * @email harsh@sdrc.co.in
 */

@Entity
@Table(name = "mst_section")
public class Sections {

    @Id
    @Column(name = "section_id_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sectionId;

    @Column(name = "section_name", nullable = false, length = 700)
    private String sectionName;

    @Column(nullable = false)
    private boolean isLive;
    
    @ManyToOne
    @JoinColumn(name = "form_id_fk", nullable = false)
    private Forms form;
    
	@Column(nullable = false)
    private int sectionOrder;

    @JoinColumn
    @ManyToOne
    private Sections parentSection;

    public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public int getSectionOrder() {
		return sectionOrder;
	}

	public void setSectionOrder(int sectionOrder) {
		this.sectionOrder = sectionOrder;
	}

	public Sections getParentSection() {
		return parentSection;
	}

	public void setParentSection(Sections parentSection) {
		this.parentSection = parentSection;
	}

	public Forms getForm() {
		return form;
	}

	public void setForm(Forms form) {
		this.form = form;
	}
	
	



}
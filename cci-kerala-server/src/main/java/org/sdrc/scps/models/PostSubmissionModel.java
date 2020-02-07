/**
 * 
 */
package org.sdrc.scps.models;

import org.sdrc.scps.domain.InmatesData;

/**
 * @author SDRC_DEV
 *
 */
public class PostSubmissionModel {

	private InmatesData inmateDetail;
	private int dcpuDistrictId;
	public InmatesData getInmateDetail() {
		return inmateDetail;
	}
	public void setInmateDetail(InmatesData inmateDetail) {
		this.inmateDetail = inmateDetail;
	}
	public int getDcpuDistrictId() {
		return dcpuDistrictId;
	}
	public void setDcpuDistrictId(int dcpuDistrictId) {
		this.dcpuDistrictId = dcpuDistrictId;
	}
	
	
	
}

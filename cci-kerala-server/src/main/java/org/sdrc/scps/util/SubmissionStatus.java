/**
 * 
 */
package org.sdrc.scps.util;

/**
 * @author Harsh Pratyush(harsh@sdrc.co.in)
 *
 */
public enum SubmissionStatus {
	APPROVED(1),PENDING(2),REJECTED(3),DRAFT(4);
	
	private final int value;

	SubmissionStatus(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
	
	public static SubmissionStatus valueOf(int status) {
		int seriesCode = status;
		for (SubmissionStatus series : values()) {
			if (series.value == seriesCode) {
				return series;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + status + "]");
	}

}

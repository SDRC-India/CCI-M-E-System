/**
 * 
 */
package org.sdrc.scps.models;

/**
 * @author Harsh Pratyush
 *
 */
public class NewsUpdateModel {

	
	private int newsUpdateId;
	
	private String newsTitle;
	
	private String newsUrl;

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
	
	
	
	
}

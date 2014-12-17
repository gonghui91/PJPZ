package com.pjpz.model;

public class Periodical {
	private String periodicalId;
	private String logoUrl;
	private String name;
	private String summary;
	private String syncTime;

	public Periodical() {
		// TODO Auto-generated constructor stub
	}

	public Periodical(String periodicalId, String logoUrl, String name,
			String summary, String syncTime) {
		super();
		this.periodicalId = periodicalId;
		this.logoUrl = logoUrl;
		this.name = name;
		this.summary = summary;
		this.syncTime = syncTime;
	}

	public String getPeriodicalId() {
		return periodicalId;
	}

	public void setPeriodicalId(String periodicalId) {
		this.periodicalId = periodicalId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}

	@Override
	public String toString() {
		return "Periodical [periodicalId=" + periodicalId + ", logoUrl="
				+ logoUrl + ", name=" + name + ", summary=" + summary
				+ ", syncTime=" + syncTime + "]";
	}

}

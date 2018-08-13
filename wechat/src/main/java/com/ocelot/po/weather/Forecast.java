package com.ocelot.po.weather;

import java.util.List;

public class Forecast {
	private String province;
	private String city;
	private String adcode;
	private String reporttime;
	private List<Casts> casts;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAdcode() {
		return adcode;
	}
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
	public String getReporttime() {
		return reporttime;
	}
	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}
	public List<Casts> getCasts() {
		return casts;
	}
	public void setCasts(List<Casts> casts) {
		this.casts = casts;
	}
	
}

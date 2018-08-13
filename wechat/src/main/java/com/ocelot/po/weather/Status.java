package com.ocelot.po.weather;

import java.util.List;

public class Status {
	private String count;
    private String status;
    private String info;
    private String infocode;
    private List<Lives> lives;
    private List<Forecast> forecasts;
	public String getInfocode() {
		return infocode;
	}
	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<Lives> getLives() {
		return lives;
	}
	public void setLives(List<Lives> lives) {
		this.lives = lives;
	}
	public List<Forecast> getForecasts() {
		return forecasts;
	}
	public void setForecasts(List<Forecast> forecasts) {
		this.forecasts = forecasts;
	}
	
}

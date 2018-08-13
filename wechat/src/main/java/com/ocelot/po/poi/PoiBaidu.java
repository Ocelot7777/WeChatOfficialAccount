package com.ocelot.po.poi;

import java.util.List;

public class PoiBaidu {
	private int status;
	private String message;	//对API访问状态值的英文说明，如果成功返回"ok"，并返回结果字段，如果失败返回错误说明。
	private int total;
	private List<Result> results;
		
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}

package com.overtech.lenovo.entity.tasklist;

/**
 * 单个资产信息
 * 
 * @author Overtech Will
 * 
 */
public class PropertyInfo {
	private String classify;
	private String brand;
	private String model;
	private String account;
	private String serial;
	private String finalDate;
	private String remark;

	public PropertyInfo(String classify, String brand, String model,
			String account, String serial, String finalDate, String remark) {
		super();
		this.classify = classify;
		this.brand = brand;
		this.model = model;
		this.account = account;
		this.serial = serial;
		this.finalDate = finalDate;
		this.remark = remark;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

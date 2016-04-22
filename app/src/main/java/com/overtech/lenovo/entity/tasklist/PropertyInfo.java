package com.overtech.lenovo.entity.tasklist;

import java.util.List;

/**
 * 资产信息解析类
 * 
 * @author Overtech Will
 * 
 */
public class PropertyInfo {
	public int st;
	public String msg;
	public Body body;
	public class Body{
		public List<Property> data;
	}
	public class Property{
		public String assert_type;
		public String brand;
		public String code;
		public String identification_code;
		public String model;
		public String quantity;
		public String remarks;
		public String repair_closing_date;
	}
}

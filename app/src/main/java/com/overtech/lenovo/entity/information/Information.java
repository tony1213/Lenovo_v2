package com.overtech.lenovo.entity.information;

/**
 * 信息
 * 
 * @author Overtech Will
 * 
 */
public class Information {
	private String avator;// 头像
	private String name;// 姓名
	private String description;// 描述
	private String[] urls;// 描述图片
	private long time;//评论发布的时间

	public Information(String avator, String name, String description,
			String[] urls,long time) {
		super();
		this.avator = avator;
		this.name = name;
		this.description = description;
		this.urls = urls;
		this.time=time;
	}

	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
}

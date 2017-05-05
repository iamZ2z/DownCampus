package com.logan.bean;

import java.util.List;

public class CampusNewsBean {
	private String title;
	private String contentType;
	private String dataTime;
	private int img_str;
	private String createTime;
	private String clickCount;

	private List<CampusNewsBean> data;






	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<CampusNewsBean> getData() {
		return data;
	}

	public void setData(List<CampusNewsBean> data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public int getImg_str() {
		return img_str;
	}

	public void setImg_str(int img_str) {
		this.img_str = img_str;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

}
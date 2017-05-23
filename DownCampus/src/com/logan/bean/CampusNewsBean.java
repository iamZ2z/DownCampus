package com.logan.bean;

import java.util.List;

public class CampusNewsBean {
    private String id;
    private String showPage;
    private String title;
    private String image;
    private String summary;
    private String updateTime;
    private int clickCount;

    private List<CampusNewsBean> data;








    public void setId(String id) {
        this.id = id;
    }

    public void setShowPage(String showPage) {
        this.showPage = showPage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void setData(List<CampusNewsBean> data) {
        this.data = data;
    }

    public int getClickCount() {
        return clickCount;
    }

    public String getId() {
        return id;
    }

    public String getShowPage() {
        return showPage;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public List<CampusNewsBean> getData() {
        return data;
    }
}
package com.logan.bean;

import java.util.List;

/**
 * Created by Z2z on 2017-4-20.
 */

public class MeetingManagerBean {
    private String name;
    private String content;
    private String createTime;

    private List<MeetingManagerBean> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public List<MeetingManagerBean> getList() {
        return data;
    }

    public void setList(List<MeetingManagerBean> data) {
        this.data = data;
    }
}

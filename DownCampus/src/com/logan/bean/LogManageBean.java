package com.logan.bean;

import java.util.List;

/**
 * Created by Z2z on 2017-4-20.
 */

public class LogManageBean {
    private String userName;
    private String logtypeStr;
    private String createTime;

    private List<LogManageBean> data;




    public String getUserName() {
        return userName;
    }

    public String getLogtypeStr() {
        return logtypeStr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public List<LogManageBean> getList() {
        return data;
    }

    public void setList(List<LogManageBean> data) {
        this.data = data;
    }
}

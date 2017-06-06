package com.logan.server;

import java.util.List;

/**
 * Created by Z2z on 2017/4/10.
 */
public class ClassScheduleListBean {
    private List<List<ClassScheduleBean>> data;


    public List<List<ClassScheduleBean>> getData() {
        return data;
    }

    public void setData(List<List<ClassScheduleBean>> data) {
        this.data = data;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

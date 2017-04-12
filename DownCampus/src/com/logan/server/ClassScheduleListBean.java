package com.logan.server;

import java.util.List;

/**
 * Created by Z2z on 2017/4/10.
 */
public class ClassScheduleListBean {
    private List<List<ClassScheduleBean>> data;
    public List<List<ClassScheduleBean>> getList() {
        return data;
    }
    public void setList(List<List<ClassScheduleBean>> list) {
        this.data = list;
    }
}

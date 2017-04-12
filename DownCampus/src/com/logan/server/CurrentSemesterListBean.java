package com.logan.server;

import java.util.List;

/**
 * Created by Z2z on 2017/4/10.
 */

public class CurrentSemesterListBean {
    private List<CurrentSemesterBean> data;
    public List<CurrentSemesterBean> getList() {
        return data;
    }
    public void setList(List<CurrentSemesterBean> list) {
        this.data = list;
    }
}

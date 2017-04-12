package com.logan.bean;

import java.util.List;

/**
 * Created by Z2z on 2017/4/7.
 */

public class AccountListBean {
    private List<AccountBean> data;
    public List<AccountBean> getList() {
        return data;
    }
    public void setList(List<AccountBean> list) {
        this.data = list;
    }
}

package com.logan.server;

import com.logan.bean.AccountBean;

import java.util.List;

/**
 * Created by Z2z on 2017/4/10.
 */

public class AccountLoginListBean {
    private List<AccountLoginBean> data;
    public List<AccountLoginBean> getList() {
        return data;
    }
    public void setList(List<AccountLoginBean> list) {
        this.data = list;
    }
}

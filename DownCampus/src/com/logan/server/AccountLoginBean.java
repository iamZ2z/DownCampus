package com.logan.server;

import java.util.List;

/**
 * Created by Z2z on 2017/4/10.
 */

public class AccountLoginBean {
    private String code;

    private String token;
    private String user_id;
    private String username;
    private String fullname;
    private String mobile;
    private String sex;
    private String picture;
    private List<AccountLoginBean> data;

    private List<AccountLoginBean> grade;

    private String grade_id;
    private String grade_name;
    private List<AccountLoginBean> clazz;

    private String clazz_id;
    private String clazz_name;




















    public String getCode() {
        return code;
    }

    public List<AccountLoginBean> getClazz() {
        return clazz;
    }

    public String getClazz_id() {
        return clazz_id;
    }

    public String getClazz_name() {
        return clazz_name;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public List<AccountLoginBean> getGrade() {
        return grade;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getSex() {
        return sex;
    }

    public String getPicture() {
        return picture;
    }

    public List<AccountLoginBean> getData() {
        return data;
    }

    public void setData(List<AccountLoginBean> data) {
        this.data = data;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }

}

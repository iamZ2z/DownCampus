package com.logan.bean;

/**
 * Created by Z2z on 2017/4/10.
 */

public class MultiSpinnerBean {
    private String name;

    private Object value;

    public MultiSpinnerBean(){
        this.name="";
        this.value="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

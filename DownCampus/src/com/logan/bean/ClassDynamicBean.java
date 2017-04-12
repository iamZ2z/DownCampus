package com.logan.bean;

import android.widget.Adapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.mobilecampus.R.id.upload2;

/**
 * Created by Z2z on 2017/3/19.
 */

public class ClassDynamicBean {

    private String head;
    private String title;
    private String time;
    private String upload;
    private String content;
    private String type_pic;
    private ListAdapter upload2;


    public List<Integer> image=new ArrayList<>();


    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public ListAdapter getUpload2() {
        return upload2;
    }

    public void setUpload2(ListAdapter upload2) {
        this.upload2 = upload2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType_pic() {
        return type_pic;
    }

    public void setType_pic(String type_pic) {
        this.type_pic = type_pic;
    }
}

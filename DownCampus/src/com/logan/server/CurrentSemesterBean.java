package com.logan.server;

/**
 * Created by Z2z on 2017/4/10.
 */
public class CurrentSemesterBean {
    private String semester_id;
    private String semester_name;
    private String semester_yearId;
    private String semester_yearName;

    private String message;


    public String getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(String semester_id) {
        this.semester_id = semester_id;
    }

    public String getSemester_name() {
        return semester_name;
    }

    public void setSemester_name(String semester_name) {
        this.semester_name = semester_name;
    }

    public String getSemester_yearId() {
        return semester_yearId;
    }

    public void setSemester_yearId(String semester_yearId) {
        this.semester_yearId = semester_yearId;
    }

    public String getSemester_yearName() {
        return semester_yearName;
    }

    public void setSemester_yearName(String semester_yearName) {
        this.semester_yearName = semester_yearName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

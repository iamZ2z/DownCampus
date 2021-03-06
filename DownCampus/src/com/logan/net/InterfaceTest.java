package com.logan.net;

/**
 * Created by Z2z on 2017-4-20.
 */

public class InterfaceTest {
    //服务器
    //final String serverurl="http://192.168.89.173:8080/iccp/api";//东东
    final String serverurl="http://lubo.logansoft.com:8080/api";//外网
    //final String serverurl="http://192.168.89.162:8080/iccp/api";//景喜
    //final String serverurl="http://192.168.89.177:8080/iccp/api";//绵科
    //final String serverurl="http://192.168.89.150:8080/iccp/api";//彦谨
    //final String serverurl="http://192.168.89.149:8080/iccp/api";//何伟
    //final String serverurl="http://192.168.8.37:8080/iccp/api";//8段内网


    //东东
    public static final String loginrole="/ums/roles.api";
    public static final String login ="/ums/checkUser.api";
    public static final String loginout ="/ums/logout.api";
    public static final String attendence ="/loadTeacherAttendance.api";
    public static final String currentterm ="/basic/getCurrentSemester.api";
    public static final String queryschedule ="/edu/querySyllabus.api";
    public static final String teacherfootprint ="/loadTeacherAttenByMonth.api";
    public static final String teachersavefootprint ="/saveTeacherAttendance.api";
    public static final String querygrade ="/getGradeOrClazzByUser.api";//暂不用，用户获取年班级
    public static final String uploademail ="/saveUserUpdateEmail.api";
    public static final String uploadautograph ="/saveUserAutograph.api";
    public static final String uploadicon ="/saveUserUpdatePicture.api";//上传头像


    private static String token;
    private static String user_id;
    private static String role;
    private static String picture;

    //彦谨
    public static final String meetingquery ="/office/meeting/query.api";
    public static final String meetingsave ="/office/meeting/save.api";
    public static final String logmanagequery ="/office/log/query.api";
    public static final String logmanagesave ="/office/log/save.api";
    public static final String leavequery ="/office/leave/query.api";
    public static final String leavesave ="/office/leave/save.api";
    public static final String leaveaudit ="/office/leave/audit.api";
    public static final String leavedelete ="/office/leave/delete.api";
    public static final String schedulequery ="/office/schedules/query.api";
    public static final String campusnewsquery ="/cms/content/query.api";
    public static final String campusactivityquery ="/cms/activity/query.api";
    public static final String organizationquery ="/office/organization/query.api";
    public static final String queryorganizationlist ="/office/user/appList.api";
    public static final String queryuser ="/office/user/query.api";

    //景喜
    public static final String parentschild ="/ums/childs.api";
    public static final String parentsattendance="/edu/attendance.api";
    public static final String parentsperformance="/edu/performance.api";
    public static final String classactivity="/edu/activity.api";
    public static final String studentleave="/edu/studentLeave.api";
    public static final String studentleavequery="/edu/queryLeave.api";
    public static final String studenthomework="/edu/homework.api";
    public static final String studentquery="/edu/queryEvaluate.api";
    public static final String studentteacherrate="/edu/evaluate.api";
    public static final String studentmyseat="/edu/querySeat.api";
    public static final String studentsubject="/common/querySubject.api";

    private static String studentId;

    //何伟
    public static final String examquery="/exam/queryExam.api";
    public static final String examachievement="/achievement/queryAchievement.api";

    //图片
    final String imgurl="http://lubo.logansoft.com:8080";//外网













    public String getImgurl() {
        return imgurl;
    }

    public static String getLogmanagesave() {
        return logmanagesave;
    }

    public static String getMeetingsave() {
        return meetingsave;
    }

    public static String getQueryorganizationlist() {
        return queryorganizationlist;
    }

    public static String getQueryuser() {
        return queryuser;
    }

    public static String getOrganizationquery() {
        return organizationquery;
    }

    public static String getUploadautograph() {
        return uploadautograph;
    }

    public static String getUploademail() {
        return uploademail;
    }

    public static String getUploadicon() {
        return uploadicon;
    }

    public static String getLeavesave() {
        return leavesave;
    }

    public static String getQuerygrade() {
        return querygrade;
    }

    public static String getTeachersavefootprint() {
        return teachersavefootprint;
    }

    public static String getTeacherfootprint() {
        return teacherfootprint;
    }

    public static String getPicture() {
        return picture;
    }

    public static void setPicture(String picture) {
        InterfaceTest.picture = picture;
    }

    public static String getExamachievement() {
        return examachievement;
    }

    public static String getExamquery() {
        return examquery;
    }

    public static String getLeaveaudit() {
        return leaveaudit;
    }

    public static String getStudentsubject() {
        return studentsubject;
    }

    public static String getStudentquery() {
        return studentquery;
    }

    public static String getStudentteacherrate() {
        return studentteacherrate;
    }

    public static String getStudentmyseat() {
        return studentmyseat;
    }

    public static String getStudenthomework() {
        return studenthomework;
    }

    public static String getStudentleavequery() {
        return studentleavequery;
    }

    public static String getStudentleave() {
        return studentleave;
    }

    public static String getClassactivity() {
        return classactivity;
    }

    public static String getParentsperformance() {
        return parentsperformance;
    }

    public static String getStudentId() {
        return studentId;
    }

    public static void setStudentId(String studentId) {
        InterfaceTest.studentId = studentId;
    }

    public static String getParentsattendance() {
        return parentsattendance;
    }

    public static String getParentschild() {
        return parentschild;
    }

    public static String getSchedulequery() {
        return schedulequery;
    }

    public static String getCampusnewsquery() {
        return campusnewsquery;
    }

    public static String getCampusactivityquery() {
        return campusactivityquery;
    }

    public static String getLeavequery() {
        return leavequery;
    }

    public static String getLogmanagequery() {
        return logmanagequery;
    }

    public static String getQueryschedule() {
        return queryschedule;
    }

    public static String getMeetingquery() {
        return meetingquery;
    }

    public static String getCurrentterm() {
        return currentterm;
    }

    public static String getAttendence() {
        return attendence;
    }

    public static String getLoginout() {
        return loginout;
    }

    public String getServerurl() {
        return serverurl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLoginrole() {
        return loginrole;
    }

    public String getLogin() {
        return login;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        InterfaceTest.role = role;
    }
}

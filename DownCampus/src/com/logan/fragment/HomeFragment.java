package com.logan.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.studentteacher.CalendarActivity;
import com.logan.acthome.studentteacher.CampusPay;
import com.logan.acthome.studentteacher.ClassActivityActivity;
import com.logan.acthome.studentteacher.ClassScheduleActivity;
import com.logan.acthome.studentteacher.ExamArrange;
import com.logan.acthome.studentteacher.HomeworkActivity;
import com.logan.acthome.studentteacher.LeaveActivity;
import com.logan.acthome.studentteacher.LogManageActivity;
import com.logan.acthome.studentteacher.MyScoreActivity;
import com.logan.acthome.studentteacher.MySeatActivity;
import com.logan.acthome.studentteacher.MySignActivity;
import com.logan.acthome.studentteacher.StudentAttendanceActivity;
import com.logan.acthome.studentteacher.TeacherRateActivity;
import com.logan.acthome.studentteacher.WorkRestActivity;
import com.logan.acthome.parentleader.BehaviorActivity;
import com.logan.acthome.parentleader.MeetingManageActivity;
import com.logan.acthome.parentleader.MyApproveActivity;
import com.logan.adapter.HomeGridAdapter;
import com.logan.bean.MeFragmentBean;
import com.logan.net.InterfaceTest;
import com.logan.net.UsuallyData;
import com.logan.server.CurrentSemesterListBean;
import com.util.viewflow.CircleFlowIndicator;
import com.util.viewflow.ImagePagerAdapter;
import com.util.viewflow.ViewFlow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements OnItemClickListener {
    private Intent mIntent;
    // 滚动图片
    private ViewFlow mViewFlow;
    private CircleFlowIndicator mFlowIndicator;
    private ArrayList<String> imageUrlList = new ArrayList<>();
    // 角色切换
    private String role;
    // 列表
    private GridView mGridView;
    private String[] str_role;
    private int[] str_role_img;
    private TypedArray mTypeArray;
    private List<Map<String, Object>> data_list;

    private String token;
    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData = new UsuallyData();
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        role = interfaceTest.getRole();
        token = interfaceTest.getToken();

        if (role.equals("老师")) {
            str_role = getResources().getStringArray(R.array.teacher);
            mTypeArray = getActivity().getResources().obtainTypedArray(R.array.teacher_img);
            str_role_img = new int[mTypeArray.length()];
            for (int i = 0; i < mTypeArray.length(); i++) {
                str_role_img[i] = mTypeArray.getResourceId(i, 0);
            }
        } else if (role.equals("学生")) {
            str_role = getResources().getStringArray(R.array.student);
            mTypeArray = getActivity().getResources().obtainTypedArray(R.array.student_img);
            str_role_img = new int[mTypeArray.length()];
            for (int i = 0; i < mTypeArray.length(); i++) {
                str_role_img[i] = mTypeArray.getResourceId(i, 0);
                //Log.v("str role img", String.valueOf(str_role_img[i]));
            }
        } else if (role.equals("家长")) {
            str_role = getResources().getStringArray(R.array.parent);
            mTypeArray = getActivity().getResources().obtainTypedArray(R.array.parent_img);
            str_role_img = new int[mTypeArray.length()];
            for (int i = 0; i < mTypeArray.length(); i++) {
                str_role_img[i] = mTypeArray.getResourceId(i, 0);
            }
        } else if (role.equals("校长")) {
            str_role = getResources().getStringArray(R.array.leader);
            mTypeArray = getActivity().getResources().obtainTypedArray(R.array.leader_img);
            str_role_img = new int[mTypeArray.length()];
            for (int i = 0; i < mTypeArray.length(); i++) {
                str_role_img[i] = mTypeArray.getResourceId(i, 0);
            }
        }
        mTypeArray.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (view==null) view = inflater.inflate(R.layout.fragment_tab1, container, false);
        mViewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        mFlowIndicator = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);
        mGridView = (GridView) view.findViewById(R.id.grid);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageUrlList.clear();
        imageUrlList.add("https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1486362762241&di" +
                "=1b34f906327e853290a43eb6372dced8&imgtype=0&src=http%3A%2F%2Fp9.qhimg" +
                ".com%2Ft012b7cc6088c4845d6.jpg");
        imageUrlList.add("https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1486362762241&di" +
                "=98e1c33626a2b0649a0cb22b1f9d994f&imgtype=0&src=http%3A%2F%2Fpic31.nipic" +
                ".com%2F20130805%2F515207_022113597000_2.jpg");
        imageUrlList.add("https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1486362762240&di" +
                "=52de895da6ad21ff546bda1c33f721c6&imgtype=0&src=http%3A%2F%2Fpic.58pic" +
                ".com%2F58pic%2F16%2F47%2F48%2F99c58PICX3U_1024.jpg");
        imageUrlList.add("https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1486362762240&di" +
                "=0bb35ab98f8c1e7448935c819c7eb729&imgtype=0&src=http%3A%2F%2Fpic.58pic" +
                ".com%2F58pic%2F13%2F66%2F66%2F02k58PICrMp_1024.jpg");
        imageUrlList.add("https://timgsa.baidu" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1486362762239&di" +
                "=d88083ed2e4baff69dfb1deab9a57a0d&imgtype=0&src=http%3A%2F%2Fpic29.photophoto" +
                ".cn%2F20131026%2F0017030267589449_b.jpg");
        initBanner(imageUrlList);

        data_list = new ArrayList<>();
        for (int i = 0; i < str_role.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("image", str_role_img[i]);
            map.put("text", str_role[i]);
            data_list.add(map);
        }
        mGridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mGridView.setAdapter(new HomeGridAdapter(getActivity(), data_list));
        mGridView.setOnItemClickListener(this);

        if (role.equals("家长")) urlkid();
        urlCurrentSemester();
    }

    private void initBanner(ArrayList<String> imageUrlList) {
        mViewFlow.setAdapter(new ImagePagerAdapter(getActivity(), imageUrlList).setInfiniteLoop
                (true));
        mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，ImageAdapter实际图片张数为3
        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = str_role[position];
        if (str.equals("我的签到")) {
            mIntent = new Intent(getActivity(), MySignActivity.class);
            startActivity(mIntent);
        } else if (str.equals("班级课表")) {
            mIntent = new Intent(getActivity(), ClassScheduleActivity.class);
            startActivity(mIntent);
        } else if (str.equals("作息安排")) {
            mIntent = new Intent(getActivity(), WorkRestActivity.class);
            startActivity(mIntent);
        } else if (str.equals("日志管理")) {
            mIntent = new Intent(getActivity(), LogManageActivity.class);
            startActivity(mIntent);
        } else if (str.equals("校园日历")) {
            mIntent = new Intent(getActivity(), CalendarActivity.class);
            startActivity(mIntent);
        } else if (str.equals("我的请假")) {
            mIntent = new Intent(getActivity(), LeaveActivity.class);
            startActivity(mIntent);
        } else if (str.equals("考试安排")) {
            mIntent = new Intent(getActivity(), ExamArrange.class);
            startActivity(mIntent);
        } else if (str.equals("我的成绩") || str.equals("学生成绩")) {
            mIntent = new Intent(getActivity(), MyScoreActivity.class);
            startActivity(mIntent);
        } else if (str.equals("我的座位")) {
            mIntent = new Intent(getActivity(), MySeatActivity.class);
            startActivity(mIntent);
        } else if (str.equals("校园缴费")) {
            mIntent = new Intent(getActivity(), CampusPay.class);
            startActivity(mIntent);
        } else if (str.equals("我的作业") || str.equals("学生作业")) {
            mIntent = new Intent(getActivity(), HomeworkActivity.class);
            startActivity(mIntent);
        } else if (str.equals("教师评价")) {
            mIntent = new Intent(getActivity(), TeacherRateActivity.class);
            startActivity(mIntent);
        } else if (str.equals("班级活动")) {
            mIntent = new Intent(getActivity(), ClassActivityActivity.class);
            startActivity(mIntent);
        } else if (str.equals("学生表现")) {
            mIntent = new Intent(getActivity(), BehaviorActivity.class);
            startActivity(mIntent);
        } else if (str.equals("学生考勤")) {
            mIntent = new Intent(getActivity(), StudentAttendanceActivity.class);
            startActivity(mIntent);
        } else if (str.equals("　审批　")) {
            mIntent = new Intent(getActivity(), MyApproveActivity.class);
            startActivity(mIntent);
        } else if (str.equals("会议管理")) {
            mIntent = new Intent(getActivity(), MeetingManageActivity.class);
            startActivity(mIntent);
        }
    }

    private void urlkid() {
        //if (interfaceTest.getStudentId().equals(null)) {
        String url = interfaceTest.getServerurl() + interfaceTest.getParentschild();
        String userid = interfaceTest.getUser_id();
        String token = interfaceTest.getToken();

        FormBody formBody = new FormBody.Builder().add("token", token).add("parentId", userid)
                .build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("url家长所有孩子的result", "请求数据:" + str);
                        final MeFragmentBean bean = new Gson().fromJson(str, MeFragmentBean.class);
                        if (bean.getData().size() != 0 || bean.getData().size() != 1) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String st = bean.getData().get(0).getStudentId();
                                    interfaceTest.setStudentId(st);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //}

    private void urlCurrentSemester() {
        String url = interfaceTest.getServerurl() + interfaceTest.getCurrentterm();
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("result", "请求数据:" + str);
                        CurrentSemesterListBean bean = new Gson().fromJson(str,
                                CurrentSemesterListBean.class);
                        Log.e("id:", bean.getList().get(0).getSemester_id());
                        //roleItem[i] = accountListBean.getList().get(i).getName();

                        ArrayList<String> arrsemesterid = new ArrayList<>();
                        ArrayList<String> arrsemestername = new ArrayList<>();
                        ArrayList<String> arrsemesteryearid = new ArrayList<>();
                        ArrayList<String> arrsemesteryearname = new ArrayList<>();
                        for (int j = 0; j < bean.getList().size(); j++) {
                            arrsemesterid.add(bean.getList().get(j).getSemester_id());
                            arrsemestername.add(bean.getList().get(j).getSemester_name());
                            arrsemesteryearid.add(bean.getList().get(j).getSemester_yearId());
                            arrsemesteryearname.add(bean.getList().get(j).getSemester_yearName());
                        }
                        usuallyData.setSemesterid(arrsemesterid);
                        usuallyData.setSemestername(arrsemestername);
                        usuallyData.setSemesteryearid(arrsemesteryearid);
                        usuallyData.setSemesteryearname(arrsemesteryearname);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

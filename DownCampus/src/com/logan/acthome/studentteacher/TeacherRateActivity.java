package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.more.RateContentActivity;
import com.logan.bean.TeacherRateBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Z2z on 2017/3/31.
 */

@ContentView(R.layout.home_teacherrate)
public class TeacherRateActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp)
    private Spinner sp;
    String[] str = {"老师a", "老师b", "老师c"};
    @ViewInject(R.id.cardview)
    private CardView cardview;

    private String[] strname;
    private String[] strteacherid;
    private String correctname = "";
    private String correctid = "";
    private ArrayList<String> mArrayList = new ArrayList<>();
    private ArrayList<String> mArrayList2 = new ArrayList<>();
    private TeacherRateBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("教师评价");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dourl();
    }

    private void sp_Year() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
                strname);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                correctid = strteacherid[position];
                correctname = strname[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentquery();
        String token = interfaceTest.getToken();
        String studentId = "";
        String role = interfaceTest.getRole();
        if (role.equals("学生")) studentId = interfaceTest.getUser_id();
        else if (role.equals("家长")) studentId = interfaceTest.getStudentId();
        Log.e("token", token);
        Log.e("studentId", studentId);

        FormBody formBody = new FormBody.Builder().add("token", token).add("studentId",
                studentId).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("RateContent的result", "请求数据:" + str);
                        bean = new Gson().fromJson(str, TeacherRateBean
                                .class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int j = 0; j < bean.getData().size(); j++) {
                                    if (bean.getData().get(j).getCode() != null) {
                                        String str1 = bean.getData().get(j).getCode();
                                        Log.e("getCode", str1);
                                    }
                                }
                                for (int n = 0; n < bean.getData().size(); n++) {
                                    if (bean.getData().get(n).getFullname() != null) {
                                        String str1 = bean.getData().get(n).getFullname();
                                        String str2 = bean.getData().get(n).getTeacherId();
                                        mArrayList.add(str1);
                                        mArrayList2.add(str2);
                                    }
                                }
                                strname = new String[mArrayList.size()];
                                strteacherid = new String[mArrayList2.size()];
                                for (int k = 0; k < mArrayList.size(); k++) {
                                    strname[k] = mArrayList.get(k);
                                    strteacherid[k] = mArrayList2.get(k);
                                    Log.e("fullname", strname[k]);
                                }


                                sp_Year();
                                cardview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(TeacherRateActivity.this,
                                                RateContentActivity.class);
                                        intent.putExtra("teacherrate", bean);
                                        intent.putExtra("teacherid", correctid);
                                        intent.putExtra("teachername", correctname);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

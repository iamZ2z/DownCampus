package com.logan.acthome.studentteacher;

import org.xutils.view.annotation.Event;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.MyScoreBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_score)
public class MyScoreActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp_year)
    private Spinner sp_year;
    String[] str_year = {"2017-02-15", "2017-02-01", "2017-03-01"};
    @ViewInject(R.id.sp_term)
    private Spinner sp_term;
    String[] str_term = {"春季", "秋季"};
    @ViewInject(R.id.sp_type)
    private Spinner sp_type;
    String[] str_type = {"全部", "周考", "月考"};
    @ViewInject(R.id.sp_name)
    private Spinner sp_name;
    String[] str_name = {"第一周周考", "第二周周考"};
    @ViewInject(R.id.btn_sure)
    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("学生成绩");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sp_Year();
        sp_Term();
        sp_Type();
        sp_Name();
    }

    private void sp_Year() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_bluebord_icon, str_year);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        sp_year.setAdapter(adapter);
        sp_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                /*
                 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void sp_Term() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_bluebord_icon, str_term);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        sp_term.setAdapter(adapter);
        sp_term.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
				/*
				 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void sp_Type() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_bluebord_icon, str_type);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
				/*
				 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void sp_Name() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_bluebord_icon, str_name);
        adapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        // 绑定 Adapter到控件
        sp_name.setAdapter(adapter);
        sp_name.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
				/*
				 * Toast.makeText(LeaveActivity.this,"你点击的是" +
				 * leave_type[position], Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    @Event(value = R.id.btn_sure)
    private void onSureClick(View v) {
        dourl();
    }

    private void dourl() {
        final MaterialDialog dialog=new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getExamachievement();
        String token = interfaceTest.getToken();

        FormBody formBody = new FormBody.Builder().add("token", token).add("pageNo", "0")
                .build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("Myapprove的result", "请求数据:" + str);
                        final MyScoreBean bean = new Gson().fromJson(str, MyScoreBean
                                .class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bean.getCode().equals("0")) {
                                    Intent intent = new Intent(MyScoreActivity.this, MyScoreDetailActivity.class);
                                    intent.putExtra("myscore", bean);
                                    startActivity(intent);
                                } else
                                    Toast.makeText(MyScoreActivity.this, "数据为空", Toast
                                            .LENGTH_SHORT).show();
                                dialog.dismiss();
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

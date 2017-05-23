package com.logan.acthome.more;

import org.xutils.view.annotation.Event;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.parentleader.SendMeetingActivity;
import com.logan.bean.SaveMeetingBean;
import com.logan.bean.WriteLogBean;
import com.logan.constant.InterfaceTest;
import com.logan.constant.UsuallyData;
import com.util.title.TitleBar;
import com.util.Utils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_log_write)
public class WriteLogActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.sp)
    private Spinner mSpinner;
    @ViewInject(R.id.tvname)
    private TextView mMultiSpinner;
    @ViewInject(R.id.logdate)
    private TextView logdate;
    private Utils util = new Utils();
    @ViewInject(R.id.etcontent)
    private EditText etcontent;

    private ArrayList<String> mArrayList;
    private ArrayList<String> mArrayListid;
    private String tvlog = "";
    private String receiverid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        spinnerListener();
    }

    private void initView() {
        titlebar.setTitle("写日志");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("发送") {
            @Override
            public void performAction(View view) {
                sendurl();
            }
        });

        logdate.setText(util.getmCalendar());
    }

    private void spinnerListener() {
        // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.log_type);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bluebord_icon,
                mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // 绑定 Adapter到控件
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String[] leave_type = getResources().getStringArray(
                        R.array.log_type);
                /*Toast.makeText(WriteLogActivity.this, "你点击的是" + leave_type[position],
                        Toast.LENGTH_SHORT).show();*/
                tvlog = leave_type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getQueryuser();
        String token = interfaceTest.getToken();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("写日志的result", "请求数据:" + str);
                        final WriteLogBean bean = new Gson().fromJson(str, WriteLogBean.class);
                        mArrayList = new ArrayList<>();
                        mArrayListid = new ArrayList<>();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < bean.getData().size(); i++) {
                                    String str = bean.getData().get(i).getFullname();
                                    mArrayList.add(str);
                                    String id = bean.getData().get(i).getId();
                                    mArrayListid.add(id);
                                }
                                diaplayData(mArrayList);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void diaplayData(ArrayList<String> mArrayList) {
        new MaterialDialog.Builder(this)
                .titleColorRes(R.color.blue_55x176x233)
                .titleGravity(GravityEnum.CENTER)
                .contentColor(getResources().getColor(R.color.black_deep))
                .positiveColorRes(R.color.blue_55x176x233)
                .title("选择写日志对象")
                .items(mArrayList)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    public boolean onSelection(MaterialDialog dialog, Integer[] which,
                                               CharSequence[] text) {
                        StringBuilder str = new StringBuilder();
                        StringBuilder strid = new StringBuilder();
                        for (int i = 0; i < which.length; i++) {
                            if (i > 0) {
                                str.append(',');
                                strid.append(',');
                            }
                            str.append(text[i]);
                            strid.append(mArrayListid.get(i));
                        }
                        mMultiSpinner.setText(str.toString());
                        receiverid = strid.toString();
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }

    @Event(value = R.id.btnadd)
    private void onbtnaddClick(View v) {
        dourl();
    }

    private void sendurl() {
        //tvlog logdata etcontent mMultiSpinner
        Log.e("id数据是", "id数据:" + receiverid);
        if (etcontent.getText().toString().equals(null) || mMultiSpinner.getText().toString()
                .equals("") || receiverid.equals(""))
            Toast.makeText(WriteLogActivity.this, "信息填写不完整", Toast.LENGTH_SHORT).show();
        else {
            InterfaceTest interfaceTest = new InterfaceTest();
            String content = etcontent.getText().toString();
            String userid = interfaceTest.getUser_id();

            String url = interfaceTest.getServerurl() + interfaceTest.getLogmanagesave();
            String token = interfaceTest.getToken();
            FormBody formBody = new FormBody.Builder().add("token", token).add
                    ("content", content).add("receiverIds", receiverid).add("receivers",
                    mMultiSpinner.getText().toString()).add("user.id", userid).add("createTime",
                    logdate.getText().toString()).build();
            final Request request = new Request.Builder().url(url).post(formBody).build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = new OkHttpClient().newCall(request).execute();
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            Log.e("发送会议的result", "请求数据:" + str);
                            final SaveMeetingBean bean = new Gson().fromJson(str, SaveMeetingBean
                                    .class);
                            mArrayList = new ArrayList<>();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bean.getCode().equals("0")) {
                                        Toast.makeText(WriteLogActivity.this, "发送成功", Toast
                                                .LENGTH_SHORT).show();
                                        finish();
                                    } else
                                        Toast.makeText(WriteLogActivity.this, "发送失败", Toast
                                                .LENGTH_SHORT).show();
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

}

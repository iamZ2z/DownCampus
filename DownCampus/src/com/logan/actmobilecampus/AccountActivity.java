package com.logan.actmobilecampus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.exceptions.HyphenateException;
import com.logan.bean.AccountListBean;
import com.logan.constant.InterfaceTest;
import com.logan.constant.UsuallyData;
import com.logan.server.AccountLoginBean;
import com.logan.widgets.AccountroleDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_account)
public class AccountActivity extends Activity {
    @ViewInject(R.id.sp_role)
    private TextView sp_role;
    @ViewInject(R.id.findpassword)
    private TextView mFindPass;
    @ViewInject(R.id.btn_loginmain)
    private Button mLoginmain;
    @ViewInject(R.id.campus)
    private TextView mCampus;
    private Intent mIntent;
    private String role = "";
    @ViewInject(R.id.et_account)
    private EditText mEditText_account;
    @ViewInject(R.id.et_password)
    private EditText mEditText_password;
    private String[] roleItem = new String[4];
    //无用
    private String token = "";
    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData = new UsuallyData();

    private EMPushConfigs emPushConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        dourl();

        try {
            EMClient.getInstance().pushManager().enableOfflinePush();
            emPushConfigs = EMClient.getInstance().pushManager().getPushConfigs();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        emPushConfigs = EMClient.getInstance().pushManager()
                                .getPushConfigsFromServer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (HyphenateException e) {
        }
    }

    @Event(value = R.id.sp_role)
    private void onSp_roleClick(View v) {
        AccountroleDialog.Builder builder = new AccountroleDialog.Builder(this);
        builder.setcloseButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setstudentButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                role = "学生";
                sp_role.setText(role);
                dialog.dismiss();
            }
        });
        builder.setparentButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                role = "家长";
                sp_role.setText(role);
                dialog.dismiss();
            }
        });
        builder.setteacherButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                role = "老师";
                sp_role.setText(role);
                dialog.dismiss();
            }
        });
        builder.setleaderButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                role = "校长";
                sp_role.setText(role);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Event(value = R.id.findpassword)
    private void onfindpasswordClick(View v) {
        startActivity(new Intent(AccountActivity.this, FindPassActivity.class));
    }

    @Event(value = R.id.btn_loginmain)
    private void onloginmainClick(View v) {
        if (mEditText_account.getText() != null && mEditText_password.getText() != null
                || sp_role.getText() != "请选择角色") {
            if (role.equals(""))
                Toast.makeText(AccountActivity.this, "请选择角色", Toast.LENGTH_SHORT).show();
            else loginurl(mEditText_account.getText().toString(), mEditText_password.getText().toString());
        } else Toast.makeText(AccountActivity.this, "登录信息未填写完整", Toast.LENGTH_SHORT).show();

    }

    @Event(value = R.id.campus)
    private void oncampusClick(View v) {
        startActivity(new Intent(AccountActivity.this, CampusCooperationActivity.class));
    }

    private void loginurl(String user, String pass) {
        String urllogin = interfaceTest.getServerurl() + interfaceTest.getLogin();
        //user pass写死
        FormBody formBody = null;
        if (role.equals("老师")) {
            formBody = new FormBody.Builder().add("roleCode", "4028882d5a5937ad015a594ff8bb0001")
                    .add("loginId", "zhoudd").add("password", "123456").build();
        } else if (role.equals("家长")) {
            formBody = new FormBody.Builder().add("roleCode", "4028882d5a5937ad015a5952e6250002")
                    .add("loginId", "1008601").add("password", "123456").build();
        } else if (role.equals("学生")) {
            formBody = new FormBody.Builder().add("roleCode", "4028d9225aabdabb015aabed33340007")
                    .add("loginId", "2017001").add("password", "123456").build();
        } else if (role.equals("校长")) {
            formBody = new FormBody.Builder().add("roleCode", "4028882d5ac6d951015ac6e544f50001")
                    .add("loginId", "sah").add("password", "123456").build();
        }

        final Request request = new Request.Builder().url(urllogin).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        AccountLoginBean bean = new Gson().fromJson(str, AccountLoginBean.class);
                        if (bean.getCode().equals("0")) {
                            token = bean.getData().get(0).getToken();
                            mIntent = new Intent(AccountActivity.this, MainActivity.class);

                            if (role.equals("学生")) {
                                interfaceTest.setStudentId(bean.getData().get(0).getUser_id());
                            } else if (role.equals("老师")) {
                                usuallyData.setCheckDate(bean.getData().get(0).getCheckDate());
                                usuallyData.setSignOutDate(bean.getData().get(0).getSignOutDate());
                            }
                            interfaceTest.setToken(token);
                            interfaceTest.setRole(role);
                            String user_id = bean.getData().get(0).getUser_id();
                            interfaceTest.setUser_id(user_id);
                            usuallyData.setFullname(bean.getData().get(0).getFullname());
                            usuallyData.setSex(bean.getData().get(0).getSex());
                            usuallyData.setOrganizationName(bean.getData().get(0).getOrganizationName());
                            usuallyData.setMobile(bean.getData().get(0).getMobile());
                            usuallyData.setEmail(bean.getData().get(0).getEmail());
                            usuallyData.setAutograph(bean.getData().get(0).getAutograph());
                            //bean.getData().get(0).getGrade().get(0).getGrade_id();
                            Log.e("gradename", bean.getData().get(0).getGrade().get(0)
                                    .getGrade_name());
                            startActivity(mIntent);
                            interfaceTest.setPicture(bean.getData().get(0).getPicture());
                        } else
                            Toast.makeText(AccountActivity.this, bean.getMessage(), Toast
                                    .LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void dourl() {
        String url = interfaceTest.getServerurl() + interfaceTest.getLoginrole();
        FormBody formBody = new FormBody.Builder().build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("ururururururr的result", "请求数据:" + str);
                        AccountListBean bean = new Gson().fromJson(str, AccountListBean.class);
                        for (int i = 0; i < bean.getList().size(); i++) {
                            Log.e("id:", bean.getList().get(i).getId());
                            Log.e("name:", bean.getList().get(i).getName());
                            roleItem[i] = bean.getList().get(i).getName();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

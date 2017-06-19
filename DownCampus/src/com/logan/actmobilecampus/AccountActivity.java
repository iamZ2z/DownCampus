package com.logan.actmobilecampus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.exceptions.HyphenateException;
import com.logan.bean.AccountListBean;
import com.logan.net.InterfaceTest;
import com.logan.net.UsuallyData;
import com.logan.server.AccountLoginBean;
import com.logan.widgets.AccountroleDialog;
import com.logan.widgets.ClearEditText;
import com.logan.widgets.PasswordEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

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
    private ClearEditText mEditText_account;
    @ViewInject(R.id.et_password)
    private PasswordEditText mEditText_password;

    private String[] roleItem = new String[4];
    private String token = "";
    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData = new UsuallyData();
    private EMPushConfigs emPushConfigs;
    private static final String space = "data";

    @ViewInject(R.id.sp_line2)
    private TextView sp_line2;
    @ViewInject(R.id.sp_line3)
    private TextView sp_line3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        dourl();
        preferenceAccount();

        //推送
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
            /*if (role.equals(""))
                Toast.makeText(AccountActivity.this, "请选择角色", Toast.LENGTH_SHORT).show();
            else*/
            loginurl(mEditText_account.getText().toString(), mEditText_password.getText()
                    .toString());
        } else Toast.makeText(AccountActivity.this, "登录信息未填写完整", Toast.LENGTH_SHORT).show();
    }

    @Event(value = R.id.campus)
    private void oncampusClick(View v) {
        startActivity(new Intent(AccountActivity.this, CampusCooperationActivity.class));
    }

    private void loginurl(final String user, final String pass) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
        String urllogin = interfaceTest.getServerurl() + interfaceTest.getLogin();
        FormBody formBody = null;
        /*if (role.equals("老师")) {
            formBody = new FormBody.Builder().add("roleCode", "4028882d5a5937ad015a594ff8bb0001")
                    .add("loginId", "170001").add("password", "123456").build();
        } else if (role.equals("家长")) {
            formBody = new FormBody.Builder().add("roleCode", "4028882d5a5937ad015a5952e6250002")
                    .add("loginId", "1008601").add("password", "123456").build();
        } else if (role.equals("学生")) {
            formBody = new FormBody.Builder().add("roleCode", "4028d9225aabdabb015aabed33340007")
                    .add("loginId", "2017001").add("password", "123456").build();
        } else if (role.equals("校长")) {
            formBody = new FormBody.Builder().add("roleCode", "4028882d5ac6d951015ac6e544f50001")
                    .add("loginId", "sah").add("password", "123456").build();
        }*/

        formBody = new FormBody.Builder().add("loginId", user).add("password", pass).build();
        final Request request = new Request.Builder().url(urllogin).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("登录成功的数据", "请求数据:" + str);
                        SharedPreferences sp = getSharedPreferences(space, Context.MODE_PRIVATE);
                        sp.edit().putString("account", user).putString("password", pass).commit();

                        final AccountLoginBean bean = new Gson().fromJson(str, AccountLoginBean
                                .class);
                        if (bean.getCode().equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                            token = bean.getData().get(0).getToken();
                            mIntent = new Intent(AccountActivity.this, MainActivity.class);

                            interfaceTest.setToken(token);
                            if (bean.getData().get(0).getRoleType().equals("0")) {
                                role = "学生";
                                interfaceTest.setRole("学生");
                            } else if (bean.getData().get(0).getRoleType().equals("1")) {
                                role = "家长";
                                interfaceTest.setRole("家长");
                            } else if (bean.getData().get(0).getRoleType().equals("2")) {
                                role = "老师";
                                interfaceTest.setRole("老师");
                            } else if (bean.getData().get(0).getRoleType().equals("4")) {
                                role = "校长";
                                interfaceTest.setRole("校长");
                            }

                            if (role.equals("学生")) {
                                interfaceTest.setStudentId(bean.getData().get(0).getUser_id());
                            } else if (role.equals("老师")) {
                                usuallyData.setCheckDate(bean.getData().get(0).getCheckDate());
                                usuallyData.setSignOutDate(bean.getData().get(0).getSignOutDate());
                            }

                            String user_id = bean.getData().get(0).getUser_id();
                            interfaceTest.setUser_id(user_id);
                            usuallyData.setFullname(bean.getData().get(0).getFullname());
                            usuallyData.setSex(bean.getData().get(0).getSex());
                            usuallyData.setOrganizationName(bean.getData().get(0)
                                    .getOrganizationName());
                            usuallyData.setMobile(bean.getData().get(0).getMobile());
                            usuallyData.setEmail(bean.getData().get(0).getEmail());
                            usuallyData.setAutograph(bean.getData().get(0).getAutograph());
                            ArrayList<String> arrgradeid = new ArrayList<>();
                            ArrayList<String> arrgradename = new ArrayList<>();
                            for (int j = 0; j < bean.getData().get(0).getGrade().size(); j++) {
                                arrgradename.add(bean.getData().get(0).getGrade().get(j).getGrade_name());
                                arrgradeid.add(bean.getData().get(0).getGrade().get(j).getGrade_id());
                            }
                            usuallyData.setGradename(arrgradename);
                            usuallyData.setGradeid(arrgradeid);
                            ArrayList<String> arrclazzid = new ArrayList<>();
                            ArrayList<String> arrclazzname = new ArrayList<>();
                            for (int j = 0; j < bean.getData().get(0).getClazz().size(); j++) {
                                arrclazzname.add(bean.getData().get(0).getClazz().get(j)
                                        .getClazz_name());
                                arrclazzid.add(bean.getData().get(0).getClazz().get(j)
                                        .getClazz_id());
                            }
                            usuallyData.setClazzname(arrclazzname);
                            usuallyData.setClazzid(arrclazzid);
                            interfaceTest.setPicture(bean.getData().get(0).getPicture());
                            startActivity(mIntent);
                            finish();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AccountActivity.this, bean.getMessage(), Toast
                                            .LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    } else runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AccountActivity.this, "登陆错误", Toast
                                    .LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
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

    private void preferenceAccount() {
        SharedPreferences sp = getSharedPreferences(space, Context.MODE_PRIVATE);
        String user = sp.getString("account", "1");
        String pass = sp.getString("password", null);
        if (!user.equals("1")) loginurl(user, pass);
    }

    @Event(value = R.id.et_password, type = View.OnKeyListener.class)
    private boolean onEtPassword(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(AccountActivity.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (mEditText_account.getText() != null && mEditText_password.getText() != null) {
                loginurl(mEditText_account.getText().toString(), mEditText_password.getText()
                        .toString());
            } else Toast.makeText(AccountActivity.this, "登录信息未填写完整", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Event(value = R.id.et_account, type = View.OnFocusChangeListener.class)
    private void onAccountFocus(View v, Boolean hasFocus) {
        if (hasFocus) sp_line2.setBackgroundColor(Color.rgb(55, 176, 233));
        else sp_line2.setBackgroundColor(Color.rgb(162, 162, 162));
    }

    @Event(value = R.id.et_password, type = View.OnFocusChangeListener.class)
    private void onPasswordFocus(View v, Boolean hasFocus) {
        if (hasFocus) sp_line3.setBackgroundColor(Color.rgb(55, 176, 233));
        else sp_line3.setBackgroundColor(Color.rgb(162, 162, 162));
    }

}

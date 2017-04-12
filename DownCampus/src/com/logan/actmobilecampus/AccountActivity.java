package com.logan.actmobilecampus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.ECApplication;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.bean.AccountListBean;
import com.logan.server.AccountLoginAsyncTask;
import com.logan.server.AccountLoginListBean;
import com.logan.widgets.CustomDialog;

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
public class AccountActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.sp_role)
    private TextView sp_role;

    @ViewInject(R.id.findpassword)
    private Button mFindPass;

    @ViewInject(R.id.btn_loginmain)
    private Button mLoginmain;

    @ViewInject(R.id.campus)
    private Button mCampus;

    private Intent mIntent;
    private String role;

    @ViewInject(R.id.et_account)
    private EditText mEditText_account;
    @ViewInject(R.id.et_password)
    private EditText mEditText_password;

    private String[] roleItem = new String[4];
    private ECApplication ecApplication;
    private String url = "http://192.168.89.173:8080/iccp/api/ums/roles.api";
    private String urllogin = "http://192.168.89.173:8080/iccp/api/ums/checkUser.api";
    private String token = "";
    private AccountLoginAsyncTask accountLoginAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        dourl();

        mFindPass.setOnClickListener(this);
        mLoginmain.setOnClickListener(this);
        mCampus.setOnClickListener(this);
    }

    @Event(value = R.id.sp_role)
    private void onSp_roleClick(View v) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findpassword:
                mIntent = new Intent(this, FindPassActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_loginmain:
                if (mEditText_account.getText() != null && mEditText_password.getText() != null
                        || sp_role.getText() != "请选择角色") {
                    loginurl(mEditText_account.getText().toString(), mEditText_password.getText()
                            .toString());
                    /*accountLoginAsyncTask = new AccountLoginAsyncTask(this, urllogin,
                            mEditText_account.getText().toString(), mEditText_password.getText()
                            .toString(),role);
                    accountLoginAsyncTask.execute();*/
                } else Toast.makeText(AccountActivity.this, "登录信息未填写完整", Toast.LENGTH_SHORT).show();
                break;
            case R.id.campus:
                mIntent = new Intent(this, CampusCooperationActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    private void loginurl(String user, String pass) {
        final OkHttpClient client = new OkHttpClient();
        //user pass写死
        FormBody formBody = new FormBody.Builder().add("roleCode",
                "4028882d5a5937ad015a594ff8bb0001").add("loginId", "zhoudd").add("password",
                "zhoudd").build();
        final Request request = new Request.Builder().url(urllogin).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        AccountLoginListBean accountListBean = gson.fromJson(str,
                                AccountLoginListBean.class);
                        token = accountListBean.getList().get(0).getToken();
                        Log.e("登录请求数据token:", accountListBean.getList().get(0).getToken());
                        mIntent = new Intent(AccountActivity.this, MainActivity.class);
                        mIntent.putExtra("role", role);
                        mIntent.putExtra("token", token);
                        Log.e("token在mIntent的值", "token为" + token);

                        //保存在constant
                        final ECApplication ecApplication=(ECApplication)getApplication();
                        ecApplication.setToken(token);

                        startActivity(mIntent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void dourl() {
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().build();
        final Request request = new Request.Builder().url(url).post(formBody).build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("result", "请求数据:" + str);
                        Gson gson = new Gson();
                        AccountListBean accountListBean = gson.fromJson(str, AccountListBean.class);
                        for (int i = 0; i < accountListBean.getList().size(); i++) {
                            Log.e("id:", accountListBean.getList().get(i).getId());
                            Log.e("name:", accountListBean.getList().get(i).getName());
                            roleItem[i] = accountListBean.getList().get(i).getName();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

package com.logan.actme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.logan.net.InterfaceTest;
import com.logan.actme.option.AboutActivity;
import com.logan.actme.option.CommentActivity;
import com.logan.actme.option.HelpActivity;
import com.logan.actme.option.NewMessageActivity;
import com.logan.actmobilecampus.AccountActivity;
import com.util.DataCleanManager;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.me_option)
public class OptionActivity extends Activity implements OnClickListener {
    private Intent mIntent;
    private TextView option_message, option_help, option_rate, option_about;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.dataclean)
    private Button btn_dataclean;
    @ViewInject(R.id.dataclean_num)
    private TextView dataclean_num;
    @ViewInject(R.id.btn_exit)
    private Button btn_exit;
    //private String url = "http://192.168.89.173:8080/iccp/api/ums/userLogout.api";
    private String url = "";
    private String token = "";
    private InterfaceTest interfaceTest = new InterfaceTest();
    private static final String space="data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        option_message.setOnClickListener(this);
        option_help.setOnClickListener(this);
        option_rate.setOnClickListener(this);
        option_about.setOnClickListener(this);

        url = interfaceTest.getServerurl() + interfaceTest.getLoginout();
        token = interfaceTest.getToken();
        Log.e("exit的token", token);

        EaseUI easeUI = EaseUI.getInstance();
        easeUI.getNotifier();
    }

    private void initView() {
        titlebar.setTitle("设置");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        option_message = (TextView) findViewById(R.id.option_message);
        option_help = (TextView) findViewById(R.id.option_help);
        option_rate = (TextView) findViewById(R.id.option_rate);
        option_about = (TextView) findViewById(R.id.option_about);
        try {
            dataclean_num.setText("" + DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Event(value = R.id.dataclean)
    private void onDataCleanClick(View v) {
        new MaterialDialog.Builder(this)
                .content("是否清除缓存")
                .positiveText("是")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        DataCleanManager.clearAllCache(OptionActivity.this);
                        try {
                            dataclean_num.setText("" + DataCleanManager.getTotalCacheSize
                                    (OptionActivity.this));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .negativeText("否")
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option_message:
                mIntent = new Intent(this, NewMessageActivity.class);
                startActivity(mIntent);
                break;
            case R.id.option_help:
                mIntent = new Intent(this, HelpActivity.class);
                startActivity(mIntent);
                break;
            case R.id.option_rate:
                mIntent = new Intent(this, CommentActivity.class);
                startActivity(mIntent);
                break;
            case R.id.option_about:
                mIntent = new Intent(this, AboutActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    @Event(value = R.id.btn_exit)
    private void onbtn_exitClick(View v) {
        final MaterialDialog dialog=new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    /*if (response.isSuccessful()) {*/
                        String str = response.body().string();
                        Log.e("OptionsAcitivity的登出数据", "数据=" + str);
                        SharedPreferences sp = getSharedPreferences(space, Context.MODE_PRIVATE);
                        sp.edit().putString("account", "1").putString("password", null).commit();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                mIntent = new Intent(OptionActivity.this, AccountActivity.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                                        .FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mIntent);
                            }
                        });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

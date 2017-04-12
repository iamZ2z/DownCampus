package com.logan.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logan.actmobilecampus.MainActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Z2z on 2017/4/10.
 */

public class AccountLoginAsyncTask extends AsyncTask<Void, Integer, Void> {
    private ProgressDialog progressDialog = null;
    private String url;
    private Context context;
    private String user;
    private String pass;
    private String token = "";
    private String role;

    public AccountLoginAsyncTask(Context context, String url, String user, String pass, String
            role) {
        this.context = context;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.role = role;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "正在登录", "Loading...", false);
    }

    @Override
    protected Void doInBackground(Void... params) {
        loginurl(user, pass);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.dismiss();
        if (token != "") {
            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("token", token);
            Log.e("token在mIntent的值", "token为" + token);
            context.startActivity(intent);
        }
    }

    private void loginurl(String user, String pass) {
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("roleCode",
                "4028882d5a5937ad015a594ff8bb0001").add("loginId", user).add("password", pass)
                .build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String str = response.body().string();
                Gson gson = new Gson();
                AccountLoginListBean accountListBean = gson.fromJson(str, AccountLoginListBean
                        .class);
                token = accountListBean.getList().get(0).getToken();
                Log.e("登录请求数据token:", accountListBean.getList().get(0).getToken());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

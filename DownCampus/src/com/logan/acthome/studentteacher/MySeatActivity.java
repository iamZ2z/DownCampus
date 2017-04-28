package com.logan.acthome.studentteacher;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.acthome.more.MySeatView;
import com.logan.bean.MySeatBean;
import com.logan.constant.InterfaceTest;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.home_myseat)
public class MySeatActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.seatview)
    private MySeatView seatview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();

        dourl();
    }

    private void initView() {
        titlebar.setTitle("我的座位");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) seatview.getLayoutParams();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        params.height = height / 2;
        seatview.setLayoutParams(params);
    }

    private void dourl() {
        InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getStudentmyseat();
        String token = interfaceTest.getToken();
        String studentId = interfaceTest.getStudentId();

        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("studentId",
                studentId).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("座位的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        final MySeatBean accountListBean = gson.fromJson(str,
                                MySeatBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int myposition = Integer.parseInt(accountListBean.getData().get
                                        (0).getSeatSort());
                                int allrow = Integer.parseInt(accountListBean.getData().get(0)
                                        .getClassNum()) / 8;
                                int seatRow = Integer.parseInt(accountListBean.getData().get(0)
                                        .getSeatRow());
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

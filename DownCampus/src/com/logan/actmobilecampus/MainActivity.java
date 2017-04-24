package com.logan.actmobilecampus;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.logan.Constant.InterfaceTest;
import com.logan.fragment.FindFragment;
import com.logan.fragment.HomeFragment;
import com.logan.fragment.MeFragment;
import com.logan.fragment.NewsFragment;
import com.logan.server.CurrentSemesterListBean;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends FragmentActivity implements OnClickListener {
    // fragment布局
    private Fragment findFragment, meFragment, newsFragment, homeFragment;
    // 记录当前fragment
    private Fragment currentFragment;

    private RelativeLayout findLayout, homeLayout, meLayout, newsLayout;
    private ImageView findImg, homeImg, meImg, newsImg;
    private TextView findTv, homeTv, meTv, newsTv;
    private TextView top_item;
    // 回调
    private int ret = 0;
    private String role, token;

    //private String url = "http://192.168.89.173:8080/iccp/api/basic/getCurrentSemester.api";
    private InterfaceTest interfaceTest=new InterfaceTest();
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initUI();
        initTab();
        // 接收回调
        if (getIntent().getIntExtra("cam", 0) != 0) ret = getIntent().getIntExtra("cam", 0);
        if (ret == 1) clickTab3Layout();
        else if (ret == 3) clickTab2Layout();
        else if (ret == 4) clickTab4Layout();

        Intent mIntent = getIntent();
        if (mIntent.getStringExtra("role") != null) role = mIntent.getStringExtra("role");
        if (mIntent.getStringExtra("token") != null) token = mIntent.getStringExtra("token");

        //初始化环信
        initEasemob();
        //加载学年学期
        initCurrentSemester();
    }

    private void initCurrentSemester() {
        url=interfaceTest.getServerurl()+interfaceTest.getCurrentterm();
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
                        Gson gson = new Gson();
                        CurrentSemesterListBean accountListBean = gson.fromJson(str,
                                CurrentSemesterListBean.class);
                        Log.e("id:", accountListBean.getList().get(0).getSemester_id());
                        Log.e("name:", accountListBean.getList().get(0).getSemester_name());
                        Log.e("idyear:", accountListBean.getList().get(0).getSemester_yearId());
                        Log.e("nameyear:", accountListBean.getList().get(0).getSemester_yearName());
                        //roleItem[i] = accountListBean.getList().get(i).getName();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initEasemob() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)
                    (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
    //以上是环信


    private void initUI() {
        homeLayout = (RelativeLayout) findViewById(R.id.rl_news);
        newsLayout = (RelativeLayout) findViewById(R.id.rl_location);
        findLayout = (RelativeLayout) findViewById(R.id.rl_me);
        meLayout = (RelativeLayout) findViewById(R.id.rl_step);

        homeLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        findLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);

        homeImg = (ImageView) findViewById(R.id.iv_home);
        newsImg = (ImageView) findViewById(R.id.iv_news);
        findImg = (ImageView) findViewById(R.id.iv_find);
        meImg = (ImageView) findViewById(R.id.iv_me);
        homeTv = (TextView) findViewById(R.id.tv_home);
        newsTv = (TextView) findViewById(R.id.tv_news);
        findTv = (TextView) findViewById(R.id.tv_find);
        meTv = (TextView) findViewById(R.id.tv_me);

        top_item = (TextView) findViewById(R.id.top_item);
    }

    private void initTab() {
        if (homeFragment == null) homeFragment = new HomeFragment();
        if (!homeFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction().add(R.id.content_layout, homeFragment)
                    .commit();
            // 记录当前Fragment
            currentFragment = homeFragment;
            // 设置图片文本的变化
            homeImg.setImageResource(R.mipmap.tag_icon_home_pressed);
            newsImg.setImageResource(R.mipmap.tag_icon_news_default);
            findImg.setImageResource(R.mipmap.tag_icon_find_default);
            meImg.setImageResource(R.mipmap.tag_icon_my_default);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_news:
                clickTab1Layout();
                break;
            case R.id.rl_location:
                clickTab2Layout();
                break;
            case R.id.rl_me:
                clickTab3Layout();
                break;
            case R.id.rl_step:
                clickTab4Layout();
                break;
            default:
                break;
        }
    }

    private void clickTab1Layout() {
        if (homeFragment == null) homeFragment = new HomeFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), homeFragment);
        // 设置底部tab变化
        homeImg.setImageResource(R.mipmap.tag_icon_home_pressed);
        newsImg.setImageResource(R.mipmap.tag_icon_news_default);
        findImg.setImageResource(R.mipmap.tag_icon_find_default);
        meImg.setImageResource(R.mipmap.tag_icon_my_default);
        top_item.setText("首页");
    }

    private void clickTab2Layout() {
        if (newsFragment == null) newsFragment = new NewsFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), newsFragment);
        homeImg.setImageResource(R.mipmap.tag_icon_home_default);
        newsImg.setImageResource(R.mipmap.tag_icon_news_pressed);
        findImg.setImageResource(R.mipmap.tag_icon_find_default);
        meImg.setImageResource(R.mipmap.tag_icon_my_default);
        top_item.setText("");
    }

    private void clickTab3Layout() {
        if (findFragment == null) findFragment = new FindFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), findFragment);
        homeImg.setImageResource(R.mipmap.tag_icon_home_default);
        newsImg.setImageResource(R.mipmap.tag_icon_news_default);
        findImg.setImageResource(R.mipmap.tag_icon_find_pressed);
        meImg.setImageResource(R.mipmap.tag_icon_my_default);
        top_item.setText("发现");
    }

    private void clickTab4Layout() {
        if (meFragment == null) meFragment = new MeFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), meFragment);
        homeImg.setImageResource(R.mipmap.tag_icon_home_default);
        newsImg.setImageResource(R.mipmap.tag_icon_news_default);
        findImg.setImageResource(R.mipmap.tag_icon_find_default);
        meImg.setImageResource(R.mipmap.tag_icon_my_pressed);
        top_item.setText("我的");
    }

    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment) return;
        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    public String getTitles() {
        return role;
    }
    public String getToken(){return token;}
}

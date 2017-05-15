package com.logan.actmobilecampus;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.NewsChat2Activity;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.util.NetUtils;
import com.logan.constant.InterfaceTest;
import com.logan.constant.UsuallyData;
import com.logan.fragment.FindFragment;
import com.logan.fragment.HomeFragment;
import com.logan.fragment.MeFragment;
import com.logan.fragment.NewsFragment;
import com.logan.server.CurrentSemesterListBean;

import org.xutils.x;

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
    private TextView tv_find, tv_home, tv_me, tv_news;
    private TextView top_item;
    // 回调
    private int ret = 0;
    private String role, token;
    private InterfaceTest interfaceTest = new InterfaceTest();
    private UsuallyData usuallyData=new UsuallyData();

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        setContentView(R.layout.activity_main);

        initUI();
        initTab();
        // 接收回调
        if (getIntent().getIntExtra("cam", 0) != 0) ret = getIntent().getIntExtra("cam", 0);
        if (ret == 1) clickTab3Layout();
        else if (ret == 3) clickTab2Layout();
        else if (ret == 4) clickTab4Layout();

        role = interfaceTest.getRole();
        token = interfaceTest.getToken();

        //初始化环信
        initEasemob();
        //加载学年学期
        initCurrentSemester();
        //环信监听
        initEaselistener();
    }

    private void initEaselistener() {
        //模拟登录
        EMClient.getInstance().login("zjhissb", "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("main", "登录聊天服务器失败！");

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

        EMClient.getInstance().addConnectionListener(new EaseConnectionListener());

        EMMessageListener msglistener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage msg : list) {
                    Log.e("Msg", msg.getFrom() + "body:" + msg.getBody().toString());
                    NotificationManager notificationManager = (NotificationManager) getSystemService
                            (Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity
                            .this);

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, msg.getFrom());
                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                            intent, 0);

                    builder.setContentTitle(msg.getFrom()).setContentText(msg.getBody().toString
                            ().substring(4)).setSmallIcon(R.mipmap.icon_logo).setWhen(System
                            .currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntent);

                    notificationManager.notify(1, builder.build());

                    Message message = new Message();
                    message.obj = 1;
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                for (EMMessage msg : list) {
                    Log.e("透传msg", msg.getFrom() + msg.getBody());
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };

        //实现ConnectionListener接口
        EMClient.getInstance().chatManager().addMessageListener(msglistener);
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    private void initCurrentSemester() {
        String url = interfaceTest.getServerurl() + interfaceTest.getCurrentterm();
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
                        CurrentSemesterListBean bean = new Gson().fromJson(str,
                                CurrentSemesterListBean.class);
                        Log.e("id:", bean.getList().get(0).getSemester_id());
                        Log.e("name:", bean.getList().get(0).getSemester_name());
                        Log.e("idyear:", bean.getList().get(0).getSemester_yearId());
                        Log.e("nameyear:", bean.getList().get(0).getSemester_yearName());
                        //roleItem[i] = accountListBean.getList().get(i).getName();
                        usuallyData.setSemesterid(bean.getList().get(0).getSemester_id());
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
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_news = (TextView) findViewById(R.id.tv_news);
        tv_find = (TextView) findViewById(R.id.tv_find);
        tv_me = (TextView) findViewById(R.id.tv_me);

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
            tv_home.setTextColor(getResources().getColor(R.color.blue_download));
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
        tv_home.setTextColor(getResources().getColor(R.color.blue_download));
        tv_news.setTextColor(getResources().getColor(R.color.gray_162x3));
        tv_find.setTextColor(getResources().getColor(R.color.gray_162x3));
        tv_me.setTextColor(getResources().getColor(R.color.gray_162x3));
    }

    private void clickTab2Layout() {
        if (newsFragment == null) newsFragment = new NewsFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), newsFragment);
        homeImg.setImageResource(R.mipmap.tag_icon_home_default);
        newsImg.setImageResource(R.mipmap.tag_icon_news_pressed);
        findImg.setImageResource(R.mipmap.tag_icon_find_default);
        meImg.setImageResource(R.mipmap.tag_icon_my_default);
        top_item.setText("");
        tv_home.setTextColor(getResources().getColor(R.color.gray_162x3));
        tv_news.setTextColor(getResources().getColor(R.color.blue_download));
        tv_find.setTextColor(getResources().getColor(R.color.gray_162x3));
        tv_me.setTextColor(getResources().getColor(R.color.gray_162x3));
    }

    private void clickTab3Layout() {
        if (findFragment == null) findFragment = new FindFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), findFragment);
        homeImg.setImageResource(R.mipmap.tag_icon_home_default);
        newsImg.setImageResource(R.mipmap.tag_icon_news_default);
        findImg.setImageResource(R.mipmap.tag_icon_find_pressed);
        meImg.setImageResource(R.mipmap.tag_icon_my_default);
        top_item.setText("发现");
        tv_home.setTextColor(this.getResources().getColor(R.color.gray_162x3));
        tv_news.setTextColor(this.getResources().getColor(R.color.gray_162x3));
        tv_find.setTextColor(this.getResources().getColor(R.color.blue_download));
        tv_me.setTextColor(this.getResources().getColor(R.color.gray_162x3));
    }

    private void clickTab4Layout() {
        if (meFragment == null) meFragment = new MeFragment();
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), meFragment);
        homeImg.setImageResource(R.mipmap.tag_icon_home_default);
        newsImg.setImageResource(R.mipmap.tag_icon_news_default);
        findImg.setImageResource(R.mipmap.tag_icon_find_default);
        meImg.setImageResource(R.mipmap.tag_icon_my_pressed);
        top_item.setText("我的");
        tv_home.setTextColor(this.getResources().getColor(R.color.gray_162x3));
        tv_news.setTextColor(this.getResources().getColor(R.color.gray_162x3));
        tv_find.setTextColor(this.getResources().getColor(R.color.gray_162x3));
        tv_me.setTextColor(this.getResources().getColor(R.color.blue_download));
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


    private class EaseConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Log.e("注册连接状态监听", "注册连接状态监听");
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Toast.makeText(MainActivity.this, "帐号在其他设备登录", Toast.LENGTH_SHORT).show();
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this));//连接不到聊天服务器
                        else
                            //当前网络不可用，请检查网络设置
                            Toast.makeText(MainActivity.this, "当前网络不可用", Toast.LENGTH_SHORT)
                                    .show();
                    }
                }
            });
        }
    }

}

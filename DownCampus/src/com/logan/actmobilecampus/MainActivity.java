package com.logan.actmobilecampus;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.AddressActivity;
import com.example.mobilecampus.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.util.NetUtils;
import com.logan.fragment.FindFragment;
import com.logan.fragment.HomeFragment;
import com.logan.fragment.MeFragment;
import com.logan.fragment.NewsFragment;
import com.util.fragmentviewpager.BarFragmentAdapter;
import com.util.fragmentviewpager.MainBottomBar;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Z2z on 2017-6-6.
 */

public class MainActivity extends FragmentActivity {
    private MainBottomBar mMainBottomBar;
    private BarFragmentAdapter mBarFragmentAdapter;
    private ViewPager mViewPager;
    // 回调
    private int ret = 0;
    private Handler mHandler = new Handler();
    private TextView top_item;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();

        // 接收回调
        if (getIntent().getIntExtra("cam", 0) != 0) ret = getIntent().getIntExtra("cam", 0);
        //初始化环信
        initEasemob();
        //环信监听
        initEaselistener();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(mIntent);
            }
        });
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

    public void setHandler(Handler handler) {
        mHandler = handler;
    }


    private void initEaselistener() {
        //模拟登录
        EMClient.getInstance().login("zzzjh", "123456", new EMCallBack() {
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
    //以上是环信

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // new Class<?>[]{}用来传递fragment到adapter里面。
        // new Bundle[]{}对应fragment，传递到fragment的数据。
        mBarFragmentAdapter = new BarFragmentAdapter(getSupportFragmentManager(), new
                Class<?>[]{HomeFragment.class, NewsFragment.class, FindFragment.class,
                MeFragment.class,}, new Bundle[]{});
        mViewPager.setAdapter(mBarFragmentAdapter);
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(0);

        mMainBottomBar = (MainBottomBar) findViewById(R.id.bottom_tab_bar);
        mMainBottomBar.setCallBack(new MainBottomBar.BottomBarCallBack() {

            @Override
            public void call(int prevIndex, int currentIndex) {
                mViewPager.setCurrentItem(currentIndex);
            }
        });
        mMainBottomBar.setSelected(0);

        top_item=(TextView)findViewById(R.id.top_item);
        img=(ImageView)findViewById(R.id.img);
    }


    //当viewpager切换完成
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mMainBottomBar.setSelected(position);
            switch (position){
                case 0:
                    top_item.setText("首页");
                    img.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    top_item.setText("消息");
                    img.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    top_item.setText("发现");
                    img.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    top_item.setText("我的");
                    img.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

}

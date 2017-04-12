package com.easemob;

import android.os.Bundle;

import com.example.mobilecampus.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by Z2z on 2017/3/29.
 */

public class NewsChat2Activity extends EaseBaseActivity {
    private EaseChatFragment mEaseChatFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.news_newschat);

        EaseUI.getInstance().init(this,null);
        //new出EaseChatFragment或其子类的实例
        mEaseChatFragment=new EaseChatFragment();
        //传入参数
        Bundle bundle=new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);

        String str=getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        bundle.putString(EaseConstant.EXTRA_USER_ID, str);

        mEaseChatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container,mEaseChatFragment).commit();

    }

}

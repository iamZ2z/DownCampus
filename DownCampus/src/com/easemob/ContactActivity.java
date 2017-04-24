package com.easemob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.mobilecampus.R;
import com.util.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017/4/6.
 */

@ContentView(R.layout.news_contact)
public class ContactActivity extends FragmentActivity{
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ImageView mCollectView;

    private Intent mIntent;
    private ContactListFragment mContactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("通讯录");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCollectView= (ImageView) titlebar.addAction(new TitleBar.ImageAction(R.mipmap.nav_btn_add) {
            @Override
            public void performAction(View view) {
                //添加好友
                mIntent=new Intent(ContactActivity.this,VerificationActivity.class);
                startActivity(mIntent);
            }
        });

        initContact();
    }

    private void initContact() {
        if (mContactListFragment == null){
            mContactListFragment = new ContactListFragment();
        }
        if (!mContactListFragment.isAdded()){
            //通过getSupportFragmentManager启动fragment即可
            getSupportFragmentManager().beginTransaction().add(R.id.layout, mContactListFragment).commit();}
    }


}

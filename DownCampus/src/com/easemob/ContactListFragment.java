package com.easemob;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.mobilecampus.R;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.logan.actnews.OtherActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Z2z on 2017/4/6.
 */

public class ContactListFragment extends EaseContactListFragment {
    private Intent mIntent;
    private Map<String,EaseUser> contactList;

    @Override
    protected void initView() {
        super.initView();
        View v = (LinearLayout) View.inflate(getActivity(), R.layout.fragment_contact, null);
        hideTitleBar();
        registerForContextMenu(listView);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        //需要设置联系人列表才能启动fragment
        getContactList();

        

        //setContactsMap(getContacts());

        /*contactListLayout.setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {

            }
        });*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser easeUser=(EaseUser)listView.getItemAtPosition(position);
                if(easeUser!=null){
                    String username=easeUser.getUsername();
                    startActivity(new Intent(getActivity(), OtherActivity.class).putExtra("userId",username));
                }
            }
        });
    }

    public Map<String,EaseUser> getContacts(){
        Map<String,EaseUser> user=new HashMap<>();
        String username="8003";
        EaseUser easeUser=new EaseUser(username);
        user.put(username,easeUser);
        return user;
    }
}

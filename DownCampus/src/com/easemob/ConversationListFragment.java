package com.easemob;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.logan.actmobilecampus.MainActivity;

/**
 * Created by Z2z on 2017/4/6.
 */

public class ConversationListFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();
        View v = (LinearLayout) View.inflate(getActivity(), R.layout.fragment_conversation, null);
        errorItemContainer.addView(v);
        hideTitleBar();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), "cannot chat with yourself", Toast
                            .LENGTH_SHORT).show();
                else {
                    Intent mIntent = new Intent(getActivity(), NewsChat2Activity.class);
                    mIntent.putExtra(EaseConstant.EXTRA_USER_ID, username);
                    startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        EaseUI easeUI = EaseUI.getInstance();
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier
                .EaseNotificationInfoProvider() {

            @Override
            public String getDisplayedText(EMMessage message) {
                String msg = EaseCommonUtils.getMessageDigest(message, getActivity());
                if (message.getType() == EMMessage.Type.TXT)
                    msg = msg + "";
                String username = "8003";
                EaseUser easeUser = new EaseUser(username);
                if (username != null) return easeUser + msg;
                else return "1";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                return 0;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                return intent;
            }
        });
    }*/

}

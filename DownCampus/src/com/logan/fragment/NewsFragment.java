package com.logan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easemob.ConversationListFragment;
import com.example.mobilecampus.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.logan.actmobilecampus.MainActivity;
import com.logan.actnews.AddressActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_tab2)
public class NewsFragment extends Fragment {
    @ViewInject(R.id.img)
    private ImageView img;

    private ConversationListFragment mConversationListFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initList();
    }

    private void initList() {
        if (mConversationListFragment == null) {
            mConversationListFragment = new ConversationListFragment();
            mConversationListFragment.hideTitleBar();
        }
        if (!mConversationListFragment.isAdded()) {
            //通过getSupportFragmentManager启动fragment即可
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id
                    .conversationlayout, mConversationListFragment).commit();
        }
    }

    @Event(R.id.img)
    private void OnImgClick(View v) {
        Intent mIntent = new Intent(getActivity(), AddressActivity.class);
        startActivity(mIntent);
    }

}

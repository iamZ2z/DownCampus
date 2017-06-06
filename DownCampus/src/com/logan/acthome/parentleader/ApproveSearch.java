package com.logan.acthome.parentleader;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017/3/31.
 */

@ContentView(R.layout.home_approvesearch)
public class ApproveSearch extends Activity{
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;

    @ViewInject(R.id.search)
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setTitle("审批搜索");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_View();
    }


    private void search_View() {
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText))
                   ; //list.setFilterText(newText);
                else
                    ;//list.clearTextFilter();
                return false;
            }
        });
    }
}

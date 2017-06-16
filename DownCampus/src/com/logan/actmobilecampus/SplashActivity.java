package com.logan.actmobilecampus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

public class SplashActivity extends Activity {
    private static final String space = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences(space, Context.MODE_PRIVATE);
        Boolean isFirstin = sp.getBoolean("isFirst", true);
        if (!isFirstin) startActivity(new Intent(this, AccountActivity.class));
        else startActivity(new Intent(this, GuideActivity.class));

        finish();
    }

}

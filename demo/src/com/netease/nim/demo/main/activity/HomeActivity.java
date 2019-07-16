package com.netease.nim.demo.main.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jrmf360.normallib.base.utils.ToastUtil;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.demo.R;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;


public class HomeActivity extends UI {
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private TextView version;
    private TextView versionGit;
    private EditText versionDate;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_home);

        //setToolBar(R.id.toolbar, R.string.app_name, R.drawable.ic_launcher);
        setTitle(R.string.app_name);

        requestBasicPermission();

        findViews();
        initViewData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findViews() {
        versionDate = findViewById(R.id.version_detail_date);
    }

    private void initViewData() {

        findViewById(R.id.button_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AVChatAction chatAction = new AVChatAction(AVChatType.VIDEO);
                //chatAction.startAudioVideoCall(AVChatType.VIDEO);
                //versionDate.setText("7e149cdfed2614688a80c8129d94ffc0");
                if (!TextUtils.isEmpty(versionDate.getText().toString())) {
                    String account = versionDate.getText().toString();

                    AVChatKit.outgoingCall(getApplicationContext(), account, UserInfoHelper.getUserDisplayName(account), AVChatType.VIDEO.getValue(), AVChatActivity.FROM_INTERNAL);
                } else {
                    ToastUtil.showLongToast(HomeActivity.this, "请输入需要呼叫的account");
                }

            }
        });
    }

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(HomeActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }
}

package com.capton.eudd;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.capton.easyupdate.EasyUpdate;
import com.capton.easyupdate.UpdateInfo;
import com.capton.ep.EasyPermission;

public class MainActivity extends AppCompatActivity {

    String url = "http://wap.apk.anzhi.com/data1/apk/201712/29/2f220d6611696dd69e20e24460bea9e9_00177600.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EasyPermission.request(this,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        ,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        final UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setForceUpate(true);
        updateInfo.setUrl(url);
        updateInfo.setFileName("蠢萌语音助手.apk");
        updateInfo.setMd5("2d6c0e90b4bd52af0f5196e23dea9b13");
        updateInfo.setCharacters("1.测试用的\n2.好了");
        updateInfo.setVersionName("1.0.1");
        updateInfo.setVersionCode(2);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyUpdate
                        .debug(true)
                        .check(MainActivity.this, updateInfo);
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


}

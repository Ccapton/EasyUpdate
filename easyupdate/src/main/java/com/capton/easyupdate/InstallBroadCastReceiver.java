package com.capton.easyupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by capton on 2018/3/2.
 */

public class InstallBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("InstallBroadCastReceiver.onReceive");
      if(EasyUpdate.INSTALL.equals(intent.getAction())){
          install(context,intent.getStringExtra("filePath"));
      }
    }

    private void install(Context context,String filePath) {
         File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    context
                    , "com.capton.easyupdate.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
             intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
}

package com.capton.easyupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * Created by capton on 2018/3/1.
 */

public class EasyUpdate {

    private static EasyUpdate sEasyUpdate;
    private static String savePath ;
    private static boolean canUpdate ;
    private static boolean sdebug = true;
    private static boolean showNotification = true;

    public static EasyUpdate savePath(String path){
        if (sEasyUpdate == null)
            sEasyUpdate = new EasyUpdate();
        if  (TextUtils.isEmpty(savePath) && !TextUtils.isEmpty(path))
            savePath = path;
        else if(TextUtils.isEmpty(path))
            System.out.println("EasyUpdate.savePath your path is empty");
        return sEasyUpdate;
    }
    public static EasyUpdate debug(boolean debug){
        sdebug = debug;
        if (sEasyUpdate == null)
            sEasyUpdate = new EasyUpdate();
        return sEasyUpdate;
    }

    public static EasyUpdate check(Context context,UpdateInfo updateInfo){
        if (sEasyUpdate == null)
            sEasyUpdate = new EasyUpdate();
        check(context,updateInfo,null);
        return sEasyUpdate;
    }

    public static EasyUpdate check(Context context,UpdateInfo updateInfo,CheckListener checkListener){
        Utils.init(context.getApplicationContext());
        if (sEasyUpdate == null)
            sEasyUpdate = new EasyUpdate();
        if (TextUtils.isEmpty(savePath)) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root, AppUtils.getAppPackageName() + "/update");
            if (!dir.exists())
                dir.mkdirs();
            savePath = dir.getAbsolutePath();
        }
        if (updateInfo == null) {
            System.out.println("EasyUpdate.check UpdateInfo is null");
            canUpdate = false;
            return sEasyUpdate;
        }
        if (TextUtils.isEmpty(updateInfo.getUrl())) {
            System.out.println("EasyUpdate.check Url is empty");
            canUpdate = false;
            return sEasyUpdate;
        }
        if (TextUtils.isEmpty(updateInfo.getMd5())) {
            System.out.println("EasyUpdate.check MD5 is empty");
            canUpdate = false;
            return sEasyUpdate;
        }
        checkResult(checkListener,updateInfo);
        showUpdateDialog(context,updateInfo,checkListener);
        return sEasyUpdate;
    }

    private static void showUpdateDialog(final Context context, final UpdateInfo updateInfo, CheckListener checkListener){
        if (sEasyUpdate == null)
            sEasyUpdate = new EasyUpdate();

        if (canUpdate && checkListener == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("版本更新 "+updateInfo.getVersionName())
                    .setMessage(updateInfo.getCharacters());
            final AlertDialog alertDialog = builder.create();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "升级", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (alertDialog.isShowing())
                        alertDialog.dismiss();
                    download(context,updateInfo);
                }
            });
            if (!updateInfo.isForceUpate()) {
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alertDialog.isShowing())
                            alertDialog.dismiss();
                    }
                });
            }else {
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
            }
            alertDialog.show();
        }
    }

    private static void checkResult(CheckListener checkListener,UpdateInfo updateInfo){
        canUpdate = true;
        if (checkListener != null)
            checkListener.handle(updateInfo);
    }

    public static String INSTALL = "easyupdate_install";
    private static void setShowNotification(Context context, long total, long current,String fileName){
        Notification.Builder builder = new Notification.Builder(context);
        builder.setProgress((int)total,(int)current,false);
        builder.setContentText("下载中 "+100*current/total+"%");
        builder.setAutoCancel(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Drawable drawable= AppUtils.getAppIcon();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            builder.setSmallIcon(Icon.createWithBitmap(bitmapDrawable.getBitmap()));
        } else {
            int iconId = context.getApplicationContext()
                    .getResources()
                    .getIdentifier("ic_launcher", "mipmap",AppUtils.getAppPackageName());
            if (iconId != 0)
               builder.setSmallIcon(iconId);
            else {
                System.out.println("EasyUpdate.setShowNotification R.mipmap.ic_launcher 不存在，请检查");
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }
         }
        builder.setContentTitle(AppUtils.getAppName());
        if (total == current) {
            builder.setContentText("下载完成，点击安装");
            Intent intent =new Intent ();
            intent.setAction(INSTALL);
            intent.putExtra("filePath",savePath+"/"+fileName);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            builder.setAutoCancel(true);

        }
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(AppUtils.getAppName()+"/update",Integer.MIN_VALUE,notification);
    }

    public static void download(Context context,UpdateInfo updateInfo){
        download(context,updateInfo,null);
    }

    public static void download(final Context context, final UpdateInfo updateInfo, final DownloadListener downloadListener){
        if (updateInfo == null)
            return;
        if (downloadListener == null) {
            showNotification = true;
        } else
            showNotification = false;

        boolean fileExist = false;
        File file = new File(savePath,updateInfo.getFileName());
        if (file.exists()) {
            String md5 = MD5Util.fileToMD5(savePath + "/" + updateInfo.getFileName());
            if (updateInfo.getMd5().equals(md5)){
                Intent intent =new Intent ();
                intent.setAction(INSTALL);
                intent.putExtra("filePath",file.getAbsolutePath());
                context.sendBroadcast(intent);
                fileExist = true;
                if(sdebug)
                    System.out.println("EasyUpdate.download 安装包已存在");
            }else {
                fileExist = false;
            }
        } else {
            fileExist = false;
        }

        if(TextUtils.isEmpty(updateInfo.getUrl())) {
            if(sdebug)
                System.out.println("EasyUpdate.download Url is empty");
            return;
        }
        if(TextUtils.isEmpty(updateInfo.getFileName())) {
            if(sdebug)
                System.out.println("EasyUpdate.download FileName is empty");
            return;
        }
        if(TextUtils.isEmpty(updateInfo.getMd5())) {
            if(sdebug)
                System.out.println("EasyUpdate.download Md5 is empty");
            return;
        }

        if(!fileExist)
        OkGo.<File>get(updateInfo.getUrl())
               .tag(context)
               .execute(new FileCallback(savePath,updateInfo.getFileName()) {
                   @Override
                   public void onSuccess(Response<File> response) {
                       String md5 = MD5Util.fileToMD5(response.body().getAbsolutePath());
                       if (md5.equals(updateInfo.getMd5())) {
                           if (downloadListener != null)
                               downloadListener.success(response);
                           if(sdebug)
                               System.out.println("EasyUpdate.onSuccess " + response.body().getAbsolutePath());
                       } else {
                           if (downloadListener != null)
                               downloadListener.error("MD5验证失败");
                           if(sdebug)
                               System.out.println("EasyUpdate.onSuccess MD5验证失败");
                       }
                   }

                   @Override
                   public File convertResponse(okhttp3.Response response) throws Throwable {
                       return super.convertResponse(response);
                   }

                   @Override
                   public void onError(Response<File> response) {
                       super.onError(response);
                       if( downloadListener != null)
                           downloadListener.error(response.message());
                   }

                   @Override
                   public void onFinish() {
                       super.onFinish();
                   }

                   @Override
                   public void downloadProgress(Progress progress) {
                       if(sdebug)
                       System.out.println("EasyUpdate.downloadProgress "+progress.currentSize);
                       if( downloadListener != null)
                           downloadListener.progress(progress);
                       if (showNotification)
                       setShowNotification(context,progress.totalSize,progress.currentSize,updateInfo.getFileName());

                   }
               });
    }

    public interface CheckListener {
        void handle(UpdateInfo updateInfo);

    }
    public interface DownloadListener{
        void progress(Progress progress);
        void success(Response<File> response);
        void error(String errorMessage);
    }
}

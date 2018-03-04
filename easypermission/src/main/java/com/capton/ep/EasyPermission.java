package com.capton.ep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

/**
 * Created by capton on 2018/2/22.
 */

public class EasyPermission {

    private static boolean debug = true;  // debug模式下输出日志

    public static void debugMode(boolean isDebugMdde){
          debug = isDebugMdde;
    }

    public interface OnPermissionListener{
        void onPermissionDenied(String permissionName);
        void onPermissionGranted(String permissionName);
    }

    private static OnPermissionListener sOnPermissionListener;
    private static String []permissionNames;
    private static final int REQUEST_CODE = 0;

    public static void request(Activity activity,OnPermissionListener onPermissionListener,String...permissionName){
        permissionNames = permissionName;
        sOnPermissionListener = onPermissionListener;
        for (int i = 0; i < permissionNames.length; i++) {
            if (!TextUtils.isEmpty(permissionNames[i])){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    startRequest(activity);
                } else {
                    return ;
                }
            }
        }
    }
    public static void request(Activity activity,String...permissionName){
        request(activity,null,permissionName);
    }

    public static void request(Fragment fragment,OnPermissionListener onPermissionListener,String...permissionName){
        permissionNames = permissionName;
        sOnPermissionListener = onPermissionListener;
        for (int i = 0; i < permissionNames.length; i++) {
            if (!TextUtils.isEmpty(permissionNames[i])){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    startRequest(fragment.getActivity());
                } else {
                    return ;
                }
            }
        }
    }
    public static void request(Fragment fragment,String...permissionName){
        request(fragment,null,permissionName);
    }

    @SuppressLint("NewApi")
    private static void startRequest(final Activity activity){
        activity.requestPermissions(permissionNames,REQUEST_CODE);

    }

    private static void callback(String permissionName){

    }


    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                if (debug)
                System.out.println("申请权限 "+permissions[i]+" 成功");
                if (sOnPermissionListener != null)
                    sOnPermissionListener.onPermissionGranted(permissions[i]);
             } else {
                if (debug)
                System.out.println("申请权限 "+permissions[i]+" 失败");
                if (sOnPermissionListener != null)
                    sOnPermissionListener.onPermissionDenied(permissions[i]);
            }
        }
    }



}

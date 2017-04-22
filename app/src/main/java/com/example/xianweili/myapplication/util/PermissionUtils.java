package com.example.xianweili.myapplication.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by xianwei li on 4/18/2017.
 */

public class PermissionUtils {

    public static final int REQ_CODE_WRITE_EXTERNAL_STORAGE = 200;

    public static boolean checkPermission(Context context, String permission){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestPermission(Activity activity, String[] permisions, int reqCode) {
        ActivityCompat.requestPermissions(activity, permisions, reqCode);
    }

    public static void requesReadExternalStoragePermission(Activity activity){
        requestPermission(activity,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQ_CODE_WRITE_EXTERNAL_STORAGE);
    }

}

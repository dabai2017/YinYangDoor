package com.dabai.TaiChi.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfo{
    private String appLable;
    private Drawable appIcon;
    private Intent intent;
    private ComponentName componentName;
    private AppInfo(){

    }
    private String getAppLable(){
        return appLable;
    }

    private void setAppLable(String appLable) {
        this.appLable = appLable;
    }

    private Drawable getAppIcon(){
        return appIcon;
    }

    private ComponentName getComponentName() {
        return componentName;
    }

    private void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public Intent getIntent(){
        return intent;
    }

    private void setIntent(Intent intent) {
        this.intent = intent;
    }

    private void setComponentName(ComponentName componentName) {
        this.componentName = componentName;
    }




    /**
     * description 通过包名获取应用图标
     * @param ApkTempPackageName
     * @return
     */
    public static Drawable getAppIconByPackageName(Context context , String ApkTempPackageName){
        Drawable drawable = null;
        try{
            drawable = context.getPackageManager().getApplicationIcon(ApkTempPackageName);
        }
        catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 通过包名获取应用名
     * @param context
     * @param packageName
     * @return
     */
    public static String getApplicationNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String Name;
        try {
            Name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Name = "";
        }
        return Name;
    }


}
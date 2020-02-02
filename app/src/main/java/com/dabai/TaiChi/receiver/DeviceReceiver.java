package com.dabai.TaiChi.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        // 设备管理：可用
        Toast.makeText(context, "阴阳门，设备管理员模式已可用", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDisabled(final Context context, Intent intent) {
        // 设备管理：不可用
        Toast.makeText(context, "阴阳门，设备管理员模式不可用", Toast.LENGTH_SHORT).show();

    }
}
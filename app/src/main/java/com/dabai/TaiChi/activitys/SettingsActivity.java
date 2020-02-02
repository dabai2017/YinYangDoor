package com.dabai.TaiChi.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dabai.TaiChi.R;
import com.dabai.TaiChi.utils.DabaiUtils;

public class SettingsActivity extends AppCompatActivity {

    private Context context;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        context = getApplicationContext();
        getSupportActionBar().setElevation(0);


        //dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private Context context;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            context = getContext();

            final Preference appinfo = getPreferenceManager().findPreference("other_version");
            final Preference setting_mode = getPreferenceManager().findPreference("setting_mode");


            appinfo.setSummary("ver : " + new DabaiUtils().getVersionName(context));
            // 0  root  shizuku  admin
            String mode = get_SharedPreferences("mode", "0");
            setting_mode.setSummary(mode);

        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {

            switch (preference.getKey()) {

                case "setting_mode":
                    checkmode();
                    break;
                case "other_feedback":
                    startActivity(new Intent(context, FeedBack.class));
                    break;
                case "other_share":
                    //判断有没有安装qrt

                    boolean isinstall = new DabaiUtils().checkApkExist(context, "com.dabai.qrtools");
                    if (isinstall) {
                        try {
                            Intent intent = new Intent();
                            //包名 包名+类名（全路径）
                            intent.setClassName("com.dabai.qrtools", "com.dabai.qrtools.BrowserOpen");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri content_url = Uri.parse("https://www.coolapk.com/apk/com.dabai.TaiChi");
                            intent.setData(content_url);
                            startActivity(intent);
                        } catch (Exception e) {
                            new DabaiUtils().sendText(context, "https://www.coolapk.com/apk/com.dabai.TaiChi");
                        }
                    }else {
                        new DabaiUtils().sendText(context, "https://www.coolapk.com/apk/com.dabai.TaiChi");
                    }

                    break;
                case "other_help":
                    new DabaiUtils().openLink(context, "https://dabai2017.gitee.io/blog/2020/02/01/%E9%98%B4%E9%98%B3%E9%97%A8%E5%B8%AE%E5%8A%A9%E5%92%8C%E6%94%AF%E6%8C%81/");
                    break;
                case "other_version":
                    new DabaiUtils().openLink(context, "https://www.coolapk.com/apk/com.dabai.TaiChi");
                    break;
                case "other_about":
                    new DabaiUtils().openLink(context, "https://dabai2017.gitee.io/blog/2020/02/01/%E9%98%B4%E9%98%B3%E9%97%A8%E5%85%B3%E4%BA%8E%E4%BF%A1%E6%81%AF/");
                    break;
                case "other_pay":
                    try {
                        Intent intent = new Intent();
                        //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        intent.setAction("android.intent.action.VIEW");
                        //支付宝二维码解析
                        Uri content_url = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=HTTPS://QR.ALIPAY.COM/FKX08574RJXQHHF1SRRFIB2");
                        intent.setData(content_url);
                        startActivity(intent);
                        Toast.makeText(context, "谢谢支持😀", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "调起支付宝失败！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            return super.onPreferenceTreeClick(preference);
        }


        /**
         * 获取SharedPreferences数据
         *
         * @param key
         * @return
         */
        public String get_SharedPreferences(String key, String moren) {
            SharedPreferences sp = context.getSharedPreferences("data", 0);
            return sp.getString(key, moren);
        }

        /**
         * 设置SharedPreferences数据
         *
         * @param key
         * @param value
         * @return
         */
        public boolean set_SharedPreferences(String key, String value) {
            SharedPreferences sp = context.getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            return editor.commit();
        }


        /**
         * 检查 工作模式
         */
        private void checkmode() {
            // 0  root  shizuku  admin
            String mode = get_SharedPreferences("mode", "0");

            int index = 0;

            if (mode.equals("root")) {
                index = 0;
            } else if (mode.equals("shizuku")) {
                index = 1;
            } else if (mode.equals("admin")) {
                index = 2;
            }

            new MaterialDialog.Builder(context)
                    .title("选择一个工作模式")
                    .positiveText("确认")
                    .neutralText("取消")
                    //暂时 取消了设备管理员模式
                    .items(new String[]{"ROOT", "Shizuku"})
                    .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            switch (which) {
                                case 0:
                                    set_SharedPreferences("mode", "root");
                                    break;
                                case 1:
                                    set_SharedPreferences("mode", "shizuku");
                                    break;
                                case 2:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        set_SharedPreferences("mode", "admin");
                                    }else {
                                        Toast.makeText(context, "当前Android版本不支持设备管理员模式!", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }

                            final Preference setting_mode = getPreferenceManager().findPreference("setting_mode");
                            String mode = get_SharedPreferences("mode", "0");
                            setting_mode.setSummary(mode);

                            return true;
                        }
                    })
                    .show();

        }

    }


}
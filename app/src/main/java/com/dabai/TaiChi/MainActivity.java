package com.dabai.TaiChi;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dabai.TaiChi.activitys.EggActivity;
import com.dabai.TaiChi.activitys.SettingsActivity;
import com.dabai.TaiChi.utils.AppInfo;
import com.dabai.TaiChi.utils.Fruit;
import com.dabai.TaiChi.utils.FruitAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //    ┃　　　┃   神兽保佑
 * //    ┃　　　┃   代码无BUG！
 * //    ┃　　　┗━━━┓
 * //    ┃　　　　　　　┣┓
 * //    ┃　　　　　　　┏┛
 * //    ┗┓┓┏━┳┓┏┛
 * //      ┃┫┫　┃┫┫
 * //      ┗┻┛　┗┻┛
 **/


public class MainActivity extends AppCompatActivity {


    ViewPager pager;
    ImageView imageview;
    TabLayout tabLayout;

    MaterialDialog pd;

    TextView te1;

    private Context context;
    private String TAG = "dabaizzz";


    private List fruitList, fruitList2;
    private RecyclerView recyclerView1, recyclerView2;
    private GridLayoutManager layoutManager, layoutManager2;
    private int apps2num;
    private Animation animation;
    private boolean ispanduan = true;
    private float mpositionOffset;
    private View page1view, page2view;

    LinearLayout errlay1;
    ImageView errorimg1;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        context = getApplicationContext();
        getSupportActionBar().setElevation(0);


        //dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sp = this.getSharedPreferences("com.dabai.TaiChi_preferences", 0);

        is_pass();

        pd = new MaterialDialog.Builder(this)
                .title("点名中")
                .content("正在翻看生死簿...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .build();


        fruitList = new ArrayList<>();
        fruitList2 = new ArrayList<>();

        init();
        first_checkmode();


    }



    /**
     * 第一次 打开 检查 工作模式
     */
    private void first_checkmode() {
        // 0  root  shizuku  admin
        String mode = get_SharedPreferences("mode", "0");

        if (mode.equals("0")){
            new MaterialDialog.Builder(this)
                    .title("选择一个工作模式")
                    .cancelable(false)
                    .positiveText("确认")
                    .neutralText("退出")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    //暂时 取消了设备管理员模式
                    .items(new String[]{"ROOT","Shizuku"})
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            switch (which){
                                case 0:
                                    set_SharedPreferences("mode","root");
                                    break;
                                case 1:
                                    set_SharedPreferences("mode","shizuku");
                                    break;
                                case 2:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        set_SharedPreferences("mode", "admin");
                                    }else {
                                        Toast.makeText(context, "当前Android版本不支持设备管理员模式!", Toast.LENGTH_SHORT).show();
                                        set_SharedPreferences("mode","shizuku");
                                    }

                                    break;
                            }
                            return true;
                        }
                    })
                    .show();
        }
    }

    /**
     * 判断应用锁定
     */
    @SuppressLint("NewApi")
    private void is_pass() {
        boolean setting_pass = sp.getBoolean("setting_pass",false);
        //是否有应用锁
        if (setting_pass){
            //P以下
            KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (km.isKeyguardSecure()) {
                Intent kmr = km.createConfirmDeviceCredentialIntent("警告", "请验证身份?");
                startActivityForResult(kmr, 888);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 888) {

        } else {
            finish();
            Toast.makeText(context, "验证身份失败!", Toast.LENGTH_SHORT).show();

        }
    }


    /*
     * 从Assets中读取图片
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 用来计算刷新的点击间隔时间
    private long refTime = 0;

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ref:

                if ((System.currentTimeMillis() - refTime) > 11000) {

                    refTime = System.currentTimeMillis();
                    f5();
                } else {
                    //弹出提示，可以有多种方式
                    new MaterialDialog.Builder(this)
                            .title("刷新被禁用")
                            .content("为避免程序过多导致卡顿，刷新按钮每10秒才能按一次。请稍后重试！")
                            .positiveText("确认")
                            .show();

                }

                break;
            case R.id.action_ser:

                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {

            if (mpositionOffset > 0.3 && mpositionOffset < 0.7) {
                startActivity(new Intent(MainActivity.this, EggActivity.class));
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                    ispanduan = true;
                }
            }).start();

        }
    };

    private void init() {

        pager = findViewById(R.id.viewpager1);
        imageview = findViewById(R.id.imageView);
        tabLayout = findViewById(R.id.tablayout);


        Bitmap bitmapimg = getImageFromAssetsFile("taichi.png");
        imageview.setImageBitmap(bitmapimg);

        List<View> viewlist = new ArrayList<>();

        page1view = LayoutInflater.from(context).inflate(R.layout.page1, null);
        page2view = LayoutInflater.from(context).inflate(R.layout.page2, null);

        errlay1 = page1view.findViewById(R.id.errorlayout);
        errorimg1 = page1view.findViewById(R.id.error1);

        Bitmap bitmap1 = getImageFromAssetsFile("guilian.png");
        errorimg1.setImageBitmap(bitmap1);


        te1 = page2view.findViewById(R.id.textView1);


        viewlist.add(page1view);
        viewlist.add(page2view);


        PagerAdapter adapter = new ViewAdapter(viewlist);

        pager.setAdapter(adapter);

        //取消动画  因为会有卡顿
        pager.setPageTransformer(true, new RotatePageTransformer());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                tabLayout.setScrollPosition(position, positionOffset, true);
                mpositionOffset = positionOffset;

                if (ispanduan && page1view.getRotation() != -20) {
                    if (mpositionOffset > 0.3 && mpositionOffset < 0.7) {
                        handler.postDelayed(task, 3000);
                        ispanduan = false;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


        /**
         * 绑定 recy 布局
         */
        recyclerView1 = page1view.findViewById(R.id.recy1);
        layoutManager = new GridLayoutManager(this, 4);
        recyclerView1.setLayoutManager(layoutManager);

        recyclerView2 = page2view.findViewById(R.id.recy2);
        layoutManager2 = new GridLayoutManager(this, 4);

        recyclerView2.setLayoutManager(layoutManager2);


        f5();

        animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        imageview.startAnimation(animation);

    }


    /**
     * 启动一个刷新所有数据的线程
     */
    private void f5() {

        //显示提示
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                /**
                 * 获取 apps 所有数据
                 */
                getApps();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         * 数据和适配器绑定
                         * 刷新recy布局
                         */

                        boolean setting_dsa = sp.getBoolean("setting_dsa",false);

                        te1.setText((setting_dsa ? "全部":"用户")+"应用 - " + apps2num);


                        FruitAdapter adapter_f1 = new FruitAdapter(fruitList);
                        recyclerView1.setAdapter(adapter_f1);

                        if (fruitList.size() > 0) {
                            errlay1.setVisibility(View.GONE);
                        } else {
                            errlay1.setVisibility(View.VISIBLE);
                        }


                        FruitAdapter adapter_f2 = new FruitAdapter(fruitList2);
                        recyclerView2.setAdapter(adapter_f2);

                        pd.dismiss();

                    }
                });

            }
        }).start();
    }


    public void getApps() {

        fruitList.clear();
        fruitList2.clear();

        List<ApplicationInfo> apps = queryFilterAppInfo();
        for (int i = 0; i < apps.size(); i++) {
            String name = apps.get(i).loadLabel(getPackageManager()).toString();
            String apppackageName = apps.get(i).packageName;
            Fruit apple = new Fruit(name, AppInfo.getAppIconByPackageName(context, apppackageName));
            fruitList.add(apple);
        }


        List<ApplicationInfo> apps2 = queryFilterAppInfo2();
        for (int i = 0; i < apps2.size(); i++) {
            String name = apps2.get(i).loadLabel(getPackageManager()).toString();
            String apppackageName = apps2.get(i).packageName;
            Fruit apple = new Fruit(name, AppInfo.getAppIconByPackageName(context, apppackageName));
            fruitList2.add(apple);
        }

        apps2num = apps2.size();

    }

    /**
     * 按照时间排序;
     * @param mList
     */
    private void sortData_time(List<ApplicationInfo> mList){

        Collections.sort(mList, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {

                PackageManager packageManager = getApplicationContext().getPackageManager();
                PackageInfo packageInfo = null;
                try {
                    packageInfo = packageManager.getPackageInfo(lhs.packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                PackageInfo packageInfo2 = null;
                try {
                    packageInfo2 = packageManager.getPackageInfo(rhs.packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                long date1 = packageInfo.firstInstallTime;
                long date2 = packageInfo2.firstInstallTime;
                // 对时间字段进行降序
                if (date1 < date2) {
                    return 1;
                }
                return -1;
            }
        });

    }

    //全部冻结的
    private List<ApplicationInfo> queryFilterAppInfo() {
        PackageManager pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> appInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        List<ApplicationInfo> applicationInfos = new ArrayList<>();

        //按照安装时间 排序
        sortData_time(appInfos);
        sortData_time(applicationInfos);


        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages = new HashSet();
        for (ResolveInfo resolveInfo : resolveinfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }


        for (ApplicationInfo app : appInfos) {

            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) >= 0)//通过flag排除系统应用，会将电话、短信也排除掉
            {
                if (!app.enabled) {
                    applicationInfos.add(app);
                    pd.setProgress(pd.getCurrentProgress());
                }
            }

        }
        return applicationInfos;
    }

    //没冻结的用户app  & 没冻结的全部app
    private List<ApplicationInfo> queryFilterAppInfo2() {
        PackageManager pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> appInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        List<ApplicationInfo> applicationInfos = new ArrayList<>();


        //按照安装时间 排序
        sortData_time(appInfos);
        sortData_time(applicationInfos);


        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages = new HashSet();
        for (ResolveInfo resolveInfo : resolveinfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }

        boolean setting_dsa = sp.getBoolean("setting_dsa",false);

        if (setting_dsa){

            for (ApplicationInfo app : appInfos) {
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) >= 0)//通过flag排除系统应用，会将电话、短信也排除掉
                {
                    if (app.enabled) {
                        applicationInfos.add(app);
                    }
                }
            }

        }else {

            for (ApplicationInfo app : appInfos) {
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)//通过flag排除系统应用，会将电话、短信也排除掉
                {
                    if (app.enabled) {
                        applicationInfos.add(app);
                    }
                }
            }

        }


        return applicationInfos;
    }


    /**
     * 获取SharedPreferences数据
     * @param key
     * @return
     */
    public String get_SharedPreferences(String key,String moren){
        SharedPreferences sp = this.getSharedPreferences("data", 0);
        return sp.getString(key, moren);
    }

    /**
     * 设置SharedPreferences数据
     * @param key
     * @param value
     * @return
     */
    public boolean set_SharedPreferences(String key,String value){
        SharedPreferences sp = this.getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        return editor.commit();
    }


}


//pages适配器
class ViewAdapter extends PagerAdapter {
    private List<View> datas;

    public ViewAdapter(List<View> list) {
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = datas.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(datas.get(position));
    }
}

//动画
class RotatePageTransformer implements ViewPager.PageTransformer {
    private static final float MAX_ROTATION = 10.0f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1)
            rotate(page, -MAX_ROTATION);
        else if (position <= 1)
            rotate(page, MAX_ROTATION * position);
        else
            rotate(page, MAX_ROTATION);
    }

    private void rotate(View view, float rotation) {
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight());
        view.setRotation(rotation);
    }

}

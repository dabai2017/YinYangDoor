package com.dabai.TaiChi.activitys;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dabai.TaiChi.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import java.io.IOException;
import java.io.InputStream;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().hide();

        //dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        //page
        addPage("你好", "欢迎你使用我们的作品。");
        addPage("权限提示", "我在APP中申请了网络权限，用来帮助我收集错误信息。这完全是匿名的，你不用担心泄露隐私。");
        addPage("工作模式", "你必须给APP选择一个工作模式，才能正常运行！");





        setBarColor(Color.parseColor("#000000"));

        setFlowAnimation();

        setIndicatorColor(Color.WHITE, Color.WHITE);

        showSkipButton(false);

    }


    private void addPage(String title, String des) {
        //page
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(title);
        sliderPage2.setDescription(des);
        sliderPage2.setTitleColor(Color.BLACK);
        sliderPage2.setDescColor(Color.BLACK);
        sliderPage2.setImageDrawable(R.drawable.taichi);
        sliderPage2.setBgColor(Color.parseColor("#f6f6f6"));
        addSlide(AppIntroFragment.newInstance(sliderPage2));
    }




    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);


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

}
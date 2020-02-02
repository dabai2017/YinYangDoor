package com.dabai.TaiChi.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.dabai.TaiChi.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import sakura.particle.Factory.BooleanFactory;
import sakura.particle.Factory.ExplodeParticleFactory;
import sakura.particle.Factory.FallingParticleFactory;
import sakura.particle.Factory.FlyawayFactory;
import sakura.particle.Factory.InnerFallingParticleFactory;
import sakura.particle.Factory.VerticalAscentFactory;
import sakura.particle.Main.ExplosionSite;

public class EggActivity extends AppCompatActivity {

    private Context context;

    ImageView img;

    //爆炸特效
    int ExplosionNum = 0;
    private ExplosionSite explosionSite1,explosionSite2,explosionSite3,explosionSite4,explosionSite5,explosionSite6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg);

        context = getApplicationContext();
        getSupportActionBar().hide();

        //dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        // 全屏展示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else {
                // 全屏显示，隐藏状态栏
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }



        Bitmap bitmapimg = getImageFromAssetsFile("taichi.png");
        img = findViewById(R.id.imageView2);
        img.setImageBitmap(bitmapimg);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        img.startAnimation(animation);


        //目前提供了六种的粒子爆炸特效
        explosionSite1 = new ExplosionSite(this, new BooleanFactory());
        explosionSite2 = new ExplosionSite(this, new ExplodeParticleFactory());
         explosionSite3 = new ExplosionSite(this, new FallingParticleFactory());
         explosionSite4 = new ExplosionSite(this, new FlyawayFactory());
         explosionSite5 = new ExplosionSite(this, new InnerFallingParticleFactory());
         explosionSite6 = new ExplosionSite(this, new VerticalAscentFactory());


        //爆炸激活方式一：将View或ViewGroup添加至雷管监听，View被点击时，触发爆炸
        //explosionSite6.addListener(img);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (ExplosionNum % 6+1){
                    case 1:
                        explosionSite1.explode(img);
                        break;
                    case 2:
                        explosionSite2.explode(img);
                        break;
                    case 3:
                        explosionSite3.explode(img);
                        break;
                    case 4:
                        explosionSite4.explode(img);
                        break;
                    case 5:
                        explosionSite5.explode(img);
                        break;
                    case 6:
                        explosionSite6.explode(img);
                        break;



                }

                ExplosionNum++;
            }
        });





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

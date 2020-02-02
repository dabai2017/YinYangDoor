package com.dabai.TaiChi.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * Created by Whitelaning on 2016/6/28.
 * Email: whitelaning@qq.com
 */
public class TaiChi extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    public TaiChi(Context context) {
        this(context, null);
    }

    public TaiChi(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaiChi(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TaiChi(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();        //创建画笔对象
        mPaint.setColor(Color.BLACK);    //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL); //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);     //设置画笔宽度为10px
        mPaint.setAntiAlias(true);     //设置抗锯齿
        mPaint.setAlpha(255);        //设置画笔透明度
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private Path path0 = new Path();
    private Path path1 = new Path();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动布局到中间
        canvas.translate(mWidth / 2, mHeight / 2);

        //画大背景颜色
        mPaint.setColor(0xffffff);
        path0.addRect(-400, -400, 400, 400, Path.Direction.CW);
        canvas.drawPath(path0, mPaint);

        mPaint.setColor(0xffffffff);
        path0.rewind();
        path0.addCircle(0, 0, 200, Path.Direction.CW);
        canvas.drawPath(path0, mPaint);

        mPaint.setColor(0xff000000);
        path1.addCircle(0, 0, 200, Path.Direction.CW);

        path0.rewind();
        path0.addRect(0, -200, 200, 200, Path.Direction.CW);
        path1.op(path0, Path.Op.DIFFERENCE);

        path0.rewind();
        path0.addCircle(0, -100, 100, Path.Direction.CW);
        path1.op(path0, Path.Op.UNION);

        path0.rewind();
        path0.addCircle(0, 100, 100, Path.Direction.CW);
        path1.op(path0, Path.Op.DIFFERENCE);
        canvas.drawPath(path1, mPaint);

        //画黑色小圆
        path0.rewind();
        path0.addCircle(0, 100, 50, Path.Direction.CW);
        mPaint.setColor(0xff000000);
        canvas.drawPath(path0, mPaint);

        //画白色小圆
        path0.rewind();
        path0.addCircle(0, -100, 50, Path.Direction.CW);
        mPaint.setColor(0xffffffff);
        canvas.drawPath(path0, mPaint);
    }
}
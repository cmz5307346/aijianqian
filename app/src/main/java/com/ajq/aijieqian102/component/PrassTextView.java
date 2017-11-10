package com.ajq.aijieqian102.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 */
public class PrassTextView extends TextView {


    public final static int MODE_ROUND_RECT = 1;

    private Paint mPaint;
    // 背景色
    private int mBgColor;
    // 点击背景色
    private int mPressBgColor;
    // 边框颜色
    private int mBorderColor;
    // 边框大小
    private float mBorderWidth;
    // 边框角半径
    private float mRadius;
    // 字体水平空隙
    private int mHorizontalPadding;
    // 字体垂直空隙
    private int mVerticalPadding;
    // 边框矩形
    private RectF mRect;
    //字体颜色
    private int mTextColor;
    //字体点击颜色
    private int mPressTextColor;

    private boolean isPress = false;


    public PrassTextView(Context context, String text) {
        super(context);
        setText(text);
        _init(context);
    }

    public PrassTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void _init(Context context) {
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 设置字体占中
        setGravity(Gravity.CENTER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置矩形边框
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景
        if (isPress) {
            if (mPressBgColor != 0)
                setTextColor(mPressTextColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mPressBgColor);
            float radius = mRadius;
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
            // 绘制边框
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mPressBgColor);
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
        } else {
            if (mTextColor != 0)
                setTextColor(mTextColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mBgColor);
            float radius = mRadius;
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
            // 绘制边框
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mBorderColor);
            canvas.drawRoundRect(mRect, radius, radius, mPaint);

        }


        super.onDraw(canvas);
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    public int getPressBgColor() {
        return mPressBgColor;
    }

    public void setPressBgColor(int pressBgColor) {
        mPressBgColor = pressBgColor;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getGetPressTextColor() {
        return mPressTextColor;
    }

    public void setPressTextColor(int mPressTextColor) {
        this.mPressTextColor = mPressTextColor;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isPress = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

}

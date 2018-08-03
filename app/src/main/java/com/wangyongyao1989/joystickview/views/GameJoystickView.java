package com.wangyongyao1989.joystickview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wangyongyao1989.joystickview.R;

/**
 * @author wangyao
 * @package com.wangyongyao1989.joystickview.views
 * @describe TODO
 * @date 2018/8/3
 */

public class GameJoystickView extends View {

    private final String TAG = GameJoystickView.class.getName();
    private Bitmap mJoystickBackground;
    private Bitmap mJoystickCenter;
    private Bitmap mJoystickCenterUP;

    private Paint mainCircle;
    private int mCenterX;
    private int mCenterY;
    private int mJoystickRadius;
    private Bitmap mJoystickCenterDown;
    private int mCenterPositionX = 0;
    private int mCenterPositionY = 0;
    private int mCenterRadius;
    private int mCenterNorH;
    private int mCenterNorW;

    private float mDownX , mDownY , mMoveX , mMoveY;
    private long mCurrentMS = 0;
    private int mTouchPositionX;
    private int mTouchPositionY;
    private long mMoveMS = 0;

    private boolean isCenterMove = false;


    public GameJoystickView(Context context) {
        super(context);
        initGameJoystickView(context);
    }

    public GameJoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGameJoystickView(context);
    }

    public GameJoystickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameJoystickView(context);
    }


    private void initGameJoystickView(Context context) {

        //获取每种状态下的bitmap
        mJoystickBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_bg_joystick);
        mJoystickCenter = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_center_nor);
        mJoystickCenterDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_center_pre);
        mJoystickCenterUP = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_center_nor);

        mCenterNorH = mJoystickCenter.getHeight();
        mCenterNorW = mJoystickCenter.getWidth();

        mCenterRadius = Math.max(mCenterNorH, mCenterNorW) / 5;

        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int d = Math.min(w, h);
        //控件中心位置坐标
        mCenterPositionX = (int) getWidth() / 2;
        mCenterPositionY = (int) getWidth() / 2;
        mTouchPositionX = mCenterPositionX;
        mTouchPositionY = mCenterPositionY;
        //获取整个控件矩形的边长的0.75倍
        mJoystickRadius = (int)( d/2 *0.75) ;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //获取view的中心点
        mCenterX = (getWidth()) / 2;
        mCenterY = (getHeight()) / 2;

        //canvas出背景图片
        canvas.drawBitmap(mJoystickBackground,null,new Rect(
                (mCenterX - mJoystickRadius),
                (mCenterY - mJoystickRadius),
                (mCenterX + mJoystickRadius),
                (mCenterY + mJoystickRadius)
        ),mainCircle);


        mCenterPositionX = mTouchPositionX;
        mCenterPositionY = mTouchPositionY;

        //canvas出中心遥感的图片
        canvas.drawBitmap(mJoystickCenter,null,new Rect(
                (mCenterPositionX - mCenterNorW/5),
                (mCenterPositionY - mCenterNorH/5),
                (mCenterPositionX +  mCenterNorW/5),
                (mCenterPositionY + mCenterNorH/5)
        ),mainCircle);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mTouchPositionX = (int) event.getX();
        mTouchPositionY = (int) event.getY();
        invalidate();

        double sqrt = Math.sqrt((mTouchPositionX - mCenterX) * (mTouchPositionX - mCenterX) +
                (mTouchPositionY - mCenterY) * (mTouchPositionY - mCenterY));
        Log.e(TAG,"sqrt；"+sqrt);
        if (sqrt > mJoystickRadius ) {
            mTouchPositionX = (int) ((mTouchPositionX - mCenterX )* mJoystickRadius / sqrt + mCenterX);
            mTouchPositionY = (int) ((mTouchPositionY - mCenterY) * mJoystickRadius / sqrt + mCenterY);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN : {
                mDownX = event.getX();
                mDownY = event.getY();

                if (sqrt > mJoystickRadius) {
                    mTouchPositionX =  mCenterX;
                    mTouchPositionY =  mCenterY;
                    return false;
                }else {
                    mJoystickCenter = mJoystickCenterDown;
                    invalidate();
                }
                mCurrentMS = System.currentTimeMillis();

            }
            break;

            case MotionEvent.ACTION_MOVE : {

                mMoveX = event.getX();
                mMoveY = event.getY();
                mMoveMS = System.currentTimeMillis();
            }
            break;

            case MotionEvent.ACTION_UP : {
                isCenterMove = false;
                mDownX = mMoveX = mDownY = mMoveY = 0;
                mMoveMS = System.currentTimeMillis();

                mTouchPositionX = (int) mCenterX;
                mTouchPositionY = (int) mCenterY;
                mJoystickCenter = mJoystickCenterUP;
                invalidate();
            }
            break;
        }

        return true;
    }


    /**
     * 控件测量的设置
     * @param measureSpec
     * @return
     */
    private int measure(int measureSpec) {
        int result = 0;

        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }


}

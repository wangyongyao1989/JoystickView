package com.wangyongyao1989.joystickview.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    private Paint mainCircle;
    private int mCenterX;
    private int mCenterY;
    private int mJoystickRadius;


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

        mJoystickBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_bg_joystick);

        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int d = Math.min(w, h);

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

        canvas.drawBitmap(mJoystickBackground,null,new Rect(
                (mCenterX - mJoystickRadius),
                (mCenterY - mJoystickRadius),
                (mCenterX + mJoystickRadius),
                (mCenterY + mJoystickRadius)
        ),mainCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

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

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

    public final static long DEFAULT_LOOP_INTERVAL = 200; // 200 ms
    private final double RAD = 57.2957795;      //1弧度=57.2957795°

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

    private int mTouchPositionX;
    private int mTouchPositionY;

    private OnJoystickMoveListener onJoystickMoveListener; // Listener
    private long loopInterval = DEFAULT_LOOP_INTERVAL;


    public final static int VIEW_CENTER_CIRCLE = 0;
    public final static int UP_DIRECTION = -1;   //上
    public final static int DOWN_DIRECTION = -2;  //下
    public final static int LEFT_DIRECTION = -3;     //左
    public final static int RIGHT_DIRECTION = -4;   //右


    private int lastPower = 0;
    private int lastAngle = 0;
    private int[] angleArray = new int[] {0, 15, 35, 55, 75, 105, 125, 145, 165, 195,
            215, 235, 255, 285, 305, 325, 345, 360};

    private Bitmap mArrowLeftNor, mArrowLeftPre, mArrowRightNor, mArrowRightPre,
            mArrowUpNor, mArrowUpPre,mArrowDownNor, mArrowDownPre, mArrowLeft, mArrowRight, mArrowUp, mArrowDown;
    private int mArrowH;
    private int mArrowW;


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

    /**
     * 遥感移动方向的监听
     */
    public interface OnJoystickMoveListener {
        void onValueChanged(int angle, int power, int direction);
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener listener,
                                          long repeatInterval) {
        this.onJoystickMoveListener = listener;
        this.loopInterval = repeatInterval;
    }


    private void initGameJoystickView(Context context) {

        //获取每种状态下的bitmap
        mJoystickBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_bg_joystick);
        mJoystickCenter = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_center_nor);
        mJoystickCenterDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_center_pre);
        mJoystickCenterUP = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_center_nor);

        mArrowLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_left_nor);
        mArrowRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_right_nor);
        mArrowUp= BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_up_nor);
        mArrowDown= BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_down_nor);


        mArrowLeftNor = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_left_nor);
        mArrowLeftPre = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_left_pre);
        mArrowRightNor = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_right_nor);
        mArrowRightPre = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_right_pre);

        mArrowUpNor= BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_up_nor);
        mArrowUpPre = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_up_pre);
        mArrowDownNor = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_down_nor);
        mArrowDownPre = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_image_joystick_down_pre);

        mArrowH = mArrowUpNor.getHeight();
        mArrowW = mArrowUpNor.getWidth();

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

        //朝上箭头
        canvas.drawBitmap(mArrowUp,null, new Rect(
                (mCenterX - mArrowW /4),
                (int) ((mCenterY - mJoystickRadius * 0.8) - mArrowH/4),
                (mCenterX + mArrowW/4),
                (int) ((mCenterY - mJoystickRadius * 0.8) + mArrowH/4)
        ),mainCircle);

        canvas.drawBitmap(mArrowDown,null, new Rect(
                (mCenterX - mArrowW /4),
                (int) ((mCenterY + mJoystickRadius * 0.8) - mArrowH/4),
                (mCenterX + mArrowW/4),
                (int) ((mCenterY + mJoystickRadius * 0.8) + mArrowH/4)
        ),mainCircle);

        canvas.drawBitmap(mArrowLeft,null, new Rect(
                (int) ((mCenterX - mJoystickRadius *0.8) - mArrowW /4),
                (mCenterY  - mArrowH/4),
                (int) ((mCenterX - mJoystickRadius *0.8) + mArrowW /4),
                (mCenterY + mArrowH/4)
        ),mainCircle);

        canvas.drawBitmap(mArrowRight,null, new Rect(
                (int) ((mCenterX + mJoystickRadius *0.8) - mArrowW /4),
                (mCenterY  - mArrowH/4),
                (int) ((mCenterX + mJoystickRadius *0.8) + mArrowW /4),
                (mCenterY + mArrowH/4)
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

        double sqrt = Math.sqrt((mTouchPositionX - mCenterX) * (mTouchPositionX - mCenterX) +
                (mTouchPositionY - mCenterY) * (mTouchPositionY - mCenterY));
//        Log.e(TAG,"sqrt；"+sqrt);

        if (sqrt > (mJoystickRadius - mCenterRadius)  ) {
            mTouchPositionX = (int) ((mTouchPositionX - mCenterX )* (mJoystickRadius - mCenterRadius) / sqrt + mCenterX);
            mTouchPositionY = (int) ((mTouchPositionY - mCenterY) * (mJoystickRadius - mCenterRadius) / sqrt + mCenterY);
        }
        changeArrowState();
        invalidate();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN : {
                if (sqrt > mJoystickRadius) {
                    mTouchPositionX =  mCenterX;
                    mTouchPositionY =  mCenterY;
                    return false;
                }else {
                    mJoystickCenter = mJoystickCenterDown;
                    invalidate();
                }

            }
            if (onJoystickMoveListener != null)
                onJoystickMoveListener.onValueChanged(getAngle(), getPower(), getFourDirection());
            break;

            case MotionEvent.ACTION_MOVE : {

                if (onJoystickMoveListener != null)
                    onJoystickMoveListener.onValueChanged(getAngle(), getPower(), getFourDirection());

            }
            break;

            case MotionEvent.ACTION_UP : {

                mTouchPositionX = (int) mCenterX;
                mTouchPositionY = (int) mCenterY;
                mJoystickCenter = mJoystickCenterUP;
                mArrowUp = mArrowUpNor;
                mArrowDown = mArrowDownNor;
                mArrowLeft = mArrowLeftNor;
                mArrowRight = mArrowRightNor;
                invalidate();
                if (onJoystickMoveListener != null)
                    onJoystickMoveListener.onValueChanged(getAngle(), getPower(), getFourDirection());
            }
            break;
        }

        return true;
    }

    /**
     * 改变箭头显示状态
     */
    private void changeArrowState() {
        if (getFourDirection() == UP_DIRECTION ) {
            mArrowUp = mArrowUpPre;
            mArrowDown = mArrowDownNor;
            mArrowLeft = mArrowLeftNor;
            mArrowRight = mArrowRightNor;
        }else if (getFourDirection() == DOWN_DIRECTION) {
            mArrowDown = mArrowDownPre;
            mArrowUp = mArrowUpNor;
            mArrowLeft = mArrowLeftNor;
            mArrowRight = mArrowRightNor;
        }else if(getFourDirection() == LEFT_DIRECTION) {
            mArrowLeft = mArrowLeftPre;
            mArrowUp = mArrowUpNor;
            mArrowDown = mArrowDownNor;
            mArrowRight = mArrowRightNor;
        }else if (getFourDirection() == RIGHT_DIRECTION) {
            mArrowRight = mArrowRightPre;
            mArrowUp = mArrowUpNor;
            mArrowDown = mArrowDownNor;
            mArrowLeft = mArrowLeftNor;
        }
    }


    /**
     *  返回四个方向的值
     * @return
     */
    private int getFourDirection() {
        int direction = VIEW_CENTER_CIRCLE;
        lastPower = getPower();
        if (lastPower <= 33 ) {
            return direction;
        }
        int a = 0;
        if (lastAngle <= 0) {
            a = (lastAngle * -1) + 90;
        } else if (lastAngle > 0) {
            if (lastAngle <= 90) {
                a = 90 - lastAngle;
            } else {
                a = 360 - (lastAngle - 90);
            }
        }
        int arraylength = angleArray.length;
        for (int i = 1; i < arraylength; i++) {
            if (a >= angleArray[i-1] && a < angleArray[i]) {
                direction = i % (arraylength - 1);
                if (direction == 0) {
                    direction++;
                }
                break;
            }
        }
        Log.e(TAG,"direction："+direction);
        if (direction > 3 && direction <= 5) {
            return UP_DIRECTION;
        }else if (direction > 5 && direction <=10) {
            return LEFT_DIRECTION;
        }else if (direction > 10 && direction <= 15) {
            return DOWN_DIRECTION;
        }else if ((direction > 0 && direction <= 3) || ((direction > 15 && direction <= 16))) {
            return RIGHT_DIRECTION;
        }else {
            return 0;
        }
    }

    /**
     * 手势移动的半径值（相对于View中心点）与背景图案半径的比值
     * @return
     */
    private int getPower() {
        return (int) (100 * Math.sqrt((mTouchPositionX - mCenterX) * (mTouchPositionX - mCenterX) +
                (mTouchPositionY - mCenterY) * (mTouchPositionY - mCenterY)) / mJoystickRadius);
    }

    /**
     * 通过坐标获取角度值（反正切值 * 弧度 = 角度）
     * @return
     */
    private int getAngle() {
        if (mTouchPositionX > mCenterX) {
            if (mTouchPositionY < mCenterY) {
                return lastAngle = (int) (Math.atan((mTouchPositionY - mCenterY) / (mTouchPositionX - mCenterX)) * RAD + 90);
            } else if (mTouchPositionY > mCenterY) {
                return lastAngle = (int) (Math.atan((mTouchPositionY - mCenterY) / (mTouchPositionX - mCenterX)) * RAD) + 90;
            } else {
                return lastAngle = 90;
            }
        } else if (mTouchPositionX < mCenterX) {
            if (mTouchPositionY < mCenterY) {
                return lastAngle = (int) (Math.atan((mTouchPositionY - mCenterY) / (mTouchPositionX - mCenterX)) * RAD - 90);
            } else if (mTouchPositionY > mCenterY) {
                return lastAngle = (int) (Math.atan((mTouchPositionY - mCenterY) / (mTouchPositionX - mCenterX)) * RAD) - 90;
            } else {
                return lastAngle = -90;
            }
        } else {
            if (mTouchPositionY <= mCenterY) {
                return lastAngle = 0;
            } else {
                if (lastAngle < 0) {
                    return lastAngle = -180;
                } else {
                    return lastAngle = 180;
                }
            }
        }
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

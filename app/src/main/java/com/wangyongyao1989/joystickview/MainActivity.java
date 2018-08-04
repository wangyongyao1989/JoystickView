package com.wangyongyao1989.joystickview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wangyongyao1989.joystickview.views.GameJoystickView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private GameJoystickView mJoystickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mJoystickView.setOnJoystickMoveListener(new GameJoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                switch (direction) {
                    case GameJoystickView.UP_DIRECTION:{
                        Log.e(TAG,"向前");
                    }
                    break;
                    case GameJoystickView.LEFT_DIRECTION :{
                        Log.e(TAG,"向左");

                    }
                    break;
                    case GameJoystickView.DOWN_DIRECTION:{
                        Log.e(TAG,"向后");

                    }
                    break;
                    case GameJoystickView.RIGHT_DIRECTION :{
                        Log.e(TAG,"向右");

                    }
                    break;

                    default: {

                    }
                    break;
                }
            }
        },GameJoystickView.DEFAULT_LOOP_INTERVAL);
    }

    private void initData() {

    }

    private void initView() {
        mJoystickView = findViewById(R.id.joystick_view);
    }
}

package com.vinnyoodles.vincent.whiteboardclient;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vinnyoodles.vincent.whiteboardclient.log.MyLog;

public class Scale {
    public static final String TAG = "ScaleImageView";
    private boolean isCanTouch = false;
    public static final float SCALE_MAX = 5.0f; //最大的缩放比例
    private static final float SCALE_MIN = 1.0f;
    private double oldDist = 0;
    private double moveDist = 0;
    private float downX1 = 0;
    private float downX2 = 0;
    private float downY1 = 0;
    private float downY2 = 0;

    private View myView;
    public Scale(View v) {myView = v;   }

//    public void setIsCanTouch(boolean canTouch) {
//        isCanTouch = canTouch;
//    }

    public void actionUP(MotionEvent event,int pointerCount){
        if (pointerCount == 2) {
            downX1 = 0;
            downY1 = 0;
            downX2 = 0;
            downY2 = 0;
        }
    }
    public void actionMove(MotionEvent event,int pointerCount){
        if (pointerCount == 2) {
            float x1 = event.getX(0);
            float x2 = event.getX(1);
            float y1 = event.getY(0);
            float y2 = event.getY(1);

            double changeX1 = x1 - downX1;
            double changeX2 = x2 - downX2;
            double changeY1 = y1 - downY1;
            double changeY2 = y2 - downY2;

            if (myView.getScaleX() > 1) { //滑动
                float lessX = (float) ((changeX1) / 2 + (changeX2) / 2);
                float lessY = (float) ((changeY1) / 2 + (changeY2) / 2);
                setSelfPivot(-lessX, -lessY);
                MyLog.v("Test","SCALE+Move");
                Log.d(TAG, "此时为滑动");
            }
            //缩放处理
            moveDist = spacing(event);
            double space = moveDist - oldDist;
            float scale = (float) (myView.getScaleX() + space / myView.getWidth());
            if (scale < SCALE_MIN) {
                MyLog.v("Test","SCALE+MIN");
                setScale(SCALE_MIN);
            } else if (scale > SCALE_MAX) {
                MyLog.v("Test","SCALE+MAX");
                setScale(SCALE_MAX);
            } else {
                MyLog.v("Test","SCALE+ELSE");
                setScale(scale);
            }
        }
    }
    public void actionPointerDown(MotionEvent event,int pointerCount){
        if (pointerCount == 2) {
            downX1 = event.getX(0);
            downX2 = event.getX(1);
            downY1 = event.getY(0);
            downY2 = event.getY(1);
            Log.d(TAG, "ACTION_POINTER_DOWN 双指按下 downX1=" + downX1 + " downX2="
                    + downX2 + "  downY1=" + downY1 + " downY2=" + downY2);
            MyLog.v("Test","ACTION_POINTER_DOWN 双指按下 downX1=" + downX1 + " downX2="
                    + downX2 + "  downY1=" + downY1 + " downY2=" + downY2);

            oldDist = spacing(event); //两点按下时的距离
        }
    }
    public void actionPointerUp(MotionEvent event,int pointerCount) {
        Log.d(TAG, "ACTION_POINTER_UP");
    }


    /**
     * 触摸使用的移动事件
     *
     * @param lessX
     * @param lessY
     */
    private void setSelfPivot(float lessX, float lessY) {
        float setPivotX = 0;
        float setPivotY = 0;
        setPivotX = myView.getPivotX() + lessX;
        setPivotY = myView.getPivotY() + lessY;
        if (setPivotX < 0 && setPivotY < 0) {
            setPivotX = 0;
            setPivotY = 0;
        } else if (setPivotX > 0 && setPivotY < 0) {
            setPivotY = 0;
            if (setPivotX > myView.getWidth()) {
                setPivotX = myView.getWidth();
            }
        } else if (setPivotX < 0 && setPivotY > 0) {
            setPivotX = 0;
            if (setPivotY > myView.getHeight()) {
                setPivotY = myView.getHeight();
            }
        } else {
            if (setPivotX > myView.getWidth()) {
                setPivotX = myView.getWidth();
            }
            if (setPivotY > myView.getHeight()) {
                setPivotY = myView.getHeight();
            }
        }
        setPivot(setPivotX, setPivotY);
    }

    /**
     * 计算两个点的距离
     *
     * @param event
     * @return
     */
    private double spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    /**
     * 平移画面，当画面的宽或高大于屏幕宽高时，调用此方法进行平移
     *
     * @param x
     * @param y
     */
    public void setPivot(float x, float y) {
        myView.setPivotX(x);
        myView.setPivotY(y);
    }


    /**
     * 设置放大缩小
     *
     * @param scale
     */
    public void setScale(float scale) {
        myView.setScaleX(scale);
        myView.setScaleY(scale);
    }

    /**
     * 初始化比例，也就是原始比例
     */
    public void setInitScale() {
        myView.setScaleX(1.0f);
        myView.setScaleY(1.0f);
        setPivot(myView.getWidth() / 2, myView.getHeight() / 2);
    }
}

package com.vinnyoodles.vincent.whiteboardclient;

/**
 * Created by vincent on 11/3/17.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vinnyoodles.vincent.whiteboardclient.log.MyLog;

import java.util.ArrayList;
import java.util.List;

public class CanvasView_bak extends View {
    static final int PEN_TYPE = 1;
    static final int ERASER_TYPE = 2;

    public List<CanvasPath> localPaths;
    public List<CanvasPath> globalPaths;
    public Bitmap immutableBitmap;
    public Bitmap bitmap;
    private Canvas localCanvas;
    private Paint paint;
    private Paint transparent;
    private int currentPaintType;
    private CanvasFragment fragment;

    private SocketEventEmitter socketEmitter;
//    private Scale scale;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    public CanvasView_bak(Context context, AttributeSet set) {
        super(context, set);

        setBackgroundColor(Color.WHITE);

        localPaths = new ArrayList<>();
        globalPaths = new ArrayList<>();
        paint = constructPaint(Color.BLACK, 10f);
        transparent = constructPaint(Color.WHITE, 25f);
        currentPaintType = PEN_TYPE;
//        scale = new Scale(this);
    }

    public void setSocketEventListener(SocketEventEmitter emitter) {
        this.socketEmitter = emitter;
    }

    public void loadBitmap(Bitmap bmp) {
        this.bitmap = bmp;
        localCanvas = new Canvas(this.bitmap);
    }

//    /**
//     * 移动时，防止图片移出屏幕
//     * 备注：图片的双指拖动是通过移动Bitmap在Canvas上的位置来实现的
//     * 图片的缩放是通过Canvas的缩放来实现Canvas上面的图片的一起缩放。
//     * 也就是说图片在实际Canvas的显示的比例是按照1:1放置的，看到的缩放效果是通过Canvas缩放从而带动图片的缩放效果
//     * 图片向左移动时，留在屏幕中的范围是图片的1/5
//     * 图片向右移动时，留在屏幕中的范围是屏幕的1/5
//     */
//    private float judgePositionX(float x, float sc) {
//        float resX = x;
//        if (x < 0 && ((mBitmapWidth + x) <= mBitmapWidth / 5)) {
//            resX = -4 * mBitmapWidth / 5;
//            mTransX = resX * sc - mCenterLeft;
//        } else if (x * sc >= (4 * mViewWidth / 5)) {
//            resX = (4 * mViewWidth / 5) / sc;
//            mTransX = resX * sc - mCenterLeft;
//        } else {
//            resX = x;
//        }
//
//        return resX;
//    }
//
//    private float judgePositionY(float y, float sc) {
//        float resY = y;
//        if (y < 0 && ((mBitmapHeight + y) <= mBitmapHeight / 5)) {
//            resY = -4 * mBitmapHeight / 5;
//            mTransY = resY * sc - mCenterTop;
//        } else if (y * sc >= 4 * mViewHeight / 5) {
//            resY = (4 * mViewHeight / 5) / sc;
//            mTransY = resY * sc - mCenterTop;
//        } else {
//            resY = y;
//        }
//        return resY;
//    }

    @Override
    public void onDraw(Canvas canvas) {
        if (immutableBitmap != null && !immutableBitmap.isRecycled()) {
            localCanvas.drawBitmap(immutableBitmap, 0, 0, paint);
            canvas.drawBitmap(immutableBitmap, 0, 0, paint);
        }
        for (CanvasPath p : localPaths)
            drawPath(p, canvas);

        for (CanvasPath p : globalPaths)
            drawPath(p, canvas);

//        canvas.scale(scale, scale);
//        x = judgePositionX(x, scale);
//        y = judgePositionY(y, scale);
    }

    public void loadFragment(CanvasFragment fragment) {
        this.fragment = fragment;
    }

//    private void drawPath(CanvasPath p, Canvas canvas) {
//        Path path = getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE ? p.landscape : p.portrait;
//        Paint curPaint = p.paint == PEN_TYPE ? paint : transparent;
//        localCanvas.drawPath(path, curPaint);
//        canvas.drawPath(path, curPaint);
//    }
    private void drawPath(CanvasPath p, Canvas canvas) {
        Path path = getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE ? p.landscape : p.portrait;
        Paint curPaint = p.paint == PEN_TYPE ? paint : transparent;
        localCanvas.drawPath(path, curPaint);
        canvas.save();
        canvas.scale(scaleX,scaleY);
        canvas.drawPath(path, curPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        int pointerCount = event.getPointerCount();
        socketEmitter.sendTouchEvent(event, currentPaintType);
        this.fragment.saveBitmap();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
//                scale.actionUP(event,pointerCount);
                break;
            case MotionEvent.ACTION_DOWN:
                if(pointerCount==2) {
                    MyLog.v("test","ACTION DOWN ~~");
                }
                else {
                    return startPath(eventX, eventY, currentPaintType, localPaths);
                }
            case MotionEvent.ACTION_MOVE:
                MyLog.v("test","Moving ~~");
                if(pointerCount==2) {
                    MyLog.v("test", "TWO POINT Moving");
//                    scale.actionMove(event,pointerCount);
//                    setScaleX(0.8f);
//                    setScaleY(0.8f);
                    scaleX = scaleX*0.9f;
                    scaleY = scaleY*0.9f;
                }else {
                    movePath(eventX, eventY, localPaths);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(pointerCount==2) {
                    MyLog.v("test", "POINTDOWN");
//                    scale.actionPointerDown(event,pointerCount);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(pointerCount==2) {
                    MyLog.v("test", "POINTUP");
//                    scale.actionPointerUp(event,pointerCount);
                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setType(int type) {
        currentPaintType = type;
    }

    public void clear() {
        localPaths.clear();
        globalPaths.clear();
        if (immutableBitmap != null)
            immutableBitmap.recycle();
        invalidate();
    }

    public boolean startPath(float x, float y, int paintType, List<CanvasPath> list) {
        int currentRotation = getResources().getConfiguration().orientation;
        CanvasPath path = new CanvasPath(paintType);

        if (currentRotation == getResources().getConfiguration().ORIENTATION_PORTRAIT)
            path.moveTo(x, y);
        else
            path.moveTo(y, x);
        list.add(path);
        return true;
    }

    public boolean movePath(float x, float y, List<CanvasPath> list) {
        if (!list.isEmpty()) {
            CanvasPath path = list.get(list.size() - 1);
            if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_PORTRAIT)
                path.lineTo(x, y);
            else
                path.lineTo(y, x);
        }
        return true;
    }

    private Paint constructPaint(int color, float width) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(width);
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
        return p;
    }
}
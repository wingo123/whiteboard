package com.vinnyoodles.vincent.whiteboardclient;

/**
 * Created by vincent on 11/3/17.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vinnyoodles.vincent.whiteboardclient.log.MyLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CanvasView extends View {
    public static final String TAG = "CanvasView";
//    private boolean isCanTouch = false;
//    public static final float SCALE_MAX = 5.0f; //最大的缩放比例
//    private static final float SCALE_MIN = 1.0f;
//    private double oldDist = 0;
//    private double moveDist = 0;
//    private float downX1 = 0;
//    private float downX2 = 0;
//    private float downY1 = 0;
//    private float downY2 = 0;
//    private float[] op1 = new float[2];
//    private float[] op2 = new float[2];
//    private float[] np1 = new float[2];
//    private float[] np2 = new float[2];
//    private int drawFlag = 0;
    private PointDraw localPointDraw = new PointDraw();
    public PointDraw globalPointDraw = new PointDraw();

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
//    private float scaleX = 1.0f;
//    private float scaleY = 1.0f;

    private Matrix matrix;
    private Matrix preMatrix;
    private float preScale = 1f;//路径设置的缩放，上次缩放后的结果
    private float curScale = 1f;//前缩放过程的状态
//    private float preOffsetX = 0f;
//    private float preOffsetY = 0f;
    private float curOffsetX = 0f;
    private float curOffsetY = 0f;
//    private float[]src1;
//    private float[]src2;
//    private float[]dst1;
//    private float[]dst2;
//    private RectF rectF;


    public CanvasView(Context context, AttributeSet set) {
        super(context, set);

        setBackgroundColor(Color.WHITE);

        localPaths = new ArrayList<>();
        globalPaths = new ArrayList<>();
        paint = constructPaint(Color.BLACK, 10f);
        transparent = constructPaint(Color.WHITE, 25f);
        currentPaintType = PEN_TYPE;
//        scale = new Scale(this);

        matrix = new Matrix();
        preMatrix = new Matrix();
//        src1 = new float[2];
//        dst1 = new float[2];
//        src2 = new float[2];
//        dst2 = new float[2];
    }

//    private void testMatrix(){
//        Matrix mt = new Matrix();
//        mt.setScale(1,2,3,4);
//        MyLog.v(TAG,"Matrix Test111:"+mt.toString());
//        mt.preScale(5,6);
//        MyLog.v(TAG,"Matrix Test222:"+mt.toString());
//        mt.postTranslate(7,8);
//        MyLog.v(TAG,"Matrix Test333:"+mt.toString());
//        Matrix mt2 = new Matrix();
//        mt.setScale(1,2,3,4);
//        mt2.setScale(4,3,3,1);
//        MyLog.v(TAG,"Matrix Test444:"+mt2.toString());
//        mt2.preConcat(mt);
//        MyLog.v(TAG,"Matrix Test555:"+mt2.toString());
//        mt2.postConcat(mt);
//        MyLog.v(TAG,"Matrix Test666:"+mt2.toString());
//    }
    public Matrix getMyMatrix(){
        return matrix;
    }
    public void setSocketEventListener(SocketEventEmitter emitter) {
        this.socketEmitter = emitter;
    }

    public void loadBitmap(Bitmap bmp) {
        this.bitmap = bmp;
        localCanvas = new Canvas(this.bitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (immutableBitmap != null && !immutableBitmap.isRecycled()) {
            canvas.save();
            Matrix tMatrix = new Matrix(preMatrix);
            tMatrix.postConcat(matrix);
            canvas.setMatrix(tMatrix);
            localCanvas.drawBitmap(immutableBitmap, 0, 0, paint);
            canvas.drawBitmap(immutableBitmap, 0, 0, paint);
            canvas.restore();
        }

        drawPaths(canvas);
//        for (CanvasPath p : localPaths)
//            drawPath(p, canvas);
//
//        for (CanvasPath p : globalPaths)
//            drawPath(p, canvas);
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

    private float distSpace(float[] p1,float[] p2){
        float x = p1[0] - p2[0];
        float y = p1[1] - p2[1];
        return (float) Math.sqrt(x * x + y * y);
    }
    public void setScale(float[] op1,float[] op2,float[] np1,float[] np2,float width,float heigth){
//        MyLog.v("Scale","PATH SET SCALE:"+ Arrays.toString(op1)+","+
//                Arrays.toString(op2)+","+Arrays.toString(np1)+","+Arrays.toString(np2));
        curScale = distSpace(np1,np2)/distSpace(op1,op2);//缩放比例
//        matrix.setScale(preScale * curScale,preScale * curScale);

//        this.src1[0] = 0f;
//        this.src1[1] = 0f;
//        this.src2[0] = width*1f;
//        this.src2[1] = heigth*1f;
//        RectF re = new RectF();
//        portrait.computeBounds(re,true);
//        this.src1[0] = re.left;
//        this.src1[1] = re.top;
//        this.src2[0] = re.right;
//        this.src2[1] = re.bottom;
//        matrix.mapPoints(dst1,src1);
//        matrix.mapPoints(dst2,src2);

//        MyLog.v("Scale","PATH SET SCALE MAPPOINT--11111:"+ Arrays.toString(src1)+","+
//                Arrays.toString(src2)+","+Arrays.toString(dst1)+","+Arrays.toString(dst2));

        curOffsetX = (np1[0]+np2[0])/2 - (op1[0]+op2[0])/2;
        curOffsetY = (np1[1]+np2[1])/2 - (op1[1]+op2[1])/2;
//        curOffsetX = curOffsetX+(width-dst2[0])/2;
//        curOffsetY = curOffsetY+(heigth-dst2[1])/2;

//        matrix.setScale(preScale * curScale,preScale * curScale,
//                (re.left + re.right)/2,(re.top + re.bottom)/2);
//        matrix.setScale(preScale * curScale,preScale * curScale,
//                (dst2[0]+dst1[0])/2,(dst2[1]+dst1[1])/2);
//        matrix.setScale(preScale * curScale,preScale * curScale,
//                width,heigth);
        matrix.setScale(curScale,curScale,width/2,heigth/2);
//        matrix.setScale(preScale * curScale,preScale * curScale,
//                width-(dst2[0]+dst1[0])/2,heigth-(dst2[1]+dst1[1])/2);
//        matrix.setScale(preScale * curScale,preScale * curScale);
//        MyLog.v("Scale","PATH SET SCALE MAPPOINT--2222:"+ Arrays.toString(src1)+","+
//                Arrays.toString(src2)+","+Arrays.toString(dst1)+","+Arrays.toString(dst2));
//        matrix.postTranslate(curOffsetX+(width-dst2[0])/2,curOffsetY+(heigth-dst2[1])/2);
        matrix.postTranslate(curOffsetX,curOffsetY);
//        matrix.postTranslate((width-dst2[0])/2,(heigth-dst2[1])/2);
    }
    private void updateOscale() {
//        MyLog.v(TAG, "bbbbbbbbbbbb:" + Arrays.toString(op1) + "," +
//                Arrays.toString(op2) + "," + Arrays.toString(np1) + "," + Arrays.toString(np2));
        preScale = preScale * curScale;
//        preOffsetX = preOffsetX + curOffsetX;
//        preOffsetY = preOffsetY + curOffsetY;
        for (CanvasPath p : localPaths) {
            p.updateOscale(matrix);
        }
        for (CanvasPath p : globalPaths) {
            p.updateOscale(matrix);
        }
        preMatrix.postConcat(matrix);
        matrix.reset();
    }
//    private void drawPath3(CanvasPath p, Canvas canvas) {
//        Path path = getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE ? p.landscape : p.portrait;
//        Paint curPaint = p.paint == PEN_TYPE ? paint : transparent;
//
//        Path tPath = new Path(path);
//        tPath.transform(p.getMatrix());
//        canvas.save();
////        canvas.scale(scaleX,scaleY);
////        canvas.setMatrix(p.getMatrix());
////        canvas.clipRect(0,0,500,500);
//        float strokeWidth = curPaint.getStrokeWidth();
//        if(p.getScale()>1) {
//            curPaint.setStrokeWidth(strokeWidth / p.getScale());//强制修改笔款，这个修改影响全局
//        }
//        canvas.drawPath(tPath, curPaint);
//        curPaint.setStrokeWidth(strokeWidth);//画完还原
//        canvas.restore();
//
//    }
    private void drawPaths(Canvas canvas){
        canvas.save();
        canvas.setMatrix(matrix);
//        localCanvas.save();
//        localCanvas.setMatrix(matrix);
        for (CanvasPath p : localPaths) {
//            canvas.setMatrix(p.getMatrix());
            drawPath(p, canvas);
        }
        for (CanvasPath p : globalPaths) {
//            canvas.setMatrix(p.getMatrix());
            drawPath(p, canvas);
        }
        canvas.restore();
//        localCanvas.restore();
    }
    private void drawPath(CanvasPath p, Canvas canvas) {
        Path path = getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE ? p.landscape : p.portrait;
        Paint curPaint = p.paint == PEN_TYPE ? paint : transparent;

        Path oPath = getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE ? p.oLandscape : p.oPortrait;
        if(oPath == null){
            oPath = path;
        }
//        localCanvas.save();
//        localCanvas.setMatrix(p.getMatrix());
////        localCanvas.clipRect(0,0,500,500);
//        localCanvas.drawPath(path, curPaint);
//        localCanvas.restore();

//        canvas.save();
//        canvas.setMatrix(p.getMatrix());
//        canvas.clipRect(0,0,500,500);
        float strokeWidth = curPaint.getStrokeWidth();
        if (preScale > 1f) {
            curPaint.setStrokeWidth(strokeWidth / p.getScale());//强制修改笔款，这个修改影响全局
        }
        localCanvas.drawPath(oPath,curPaint);
        canvas.drawPath(path, curPaint);
        curPaint.setStrokeWidth(strokeWidth);//画完还原
//        canvas.restore();
    }
    public boolean dealWithTouchEvent(int eventType, int pointerCount,
                                      float eventX, float eventY,
                                      float eventX1, float eventY1,
                                      PointDraw pointDraw,List<CanvasPath> canvasPaths,
                                      int paintType){
        float wigth = getWidth();
        float heigth = getHeight();
        switch (eventType) {
            case MotionEvent.ACTION_DOWN:
                if(pointerCount==2) {
                    MyLog.v(TAG,"ACTION DOWN ~~");
                    pointDraw.drawFlag = 0;
                }
                else {
                    pointDraw.drawFlag = 1;
                    return startPath(eventX, eventY, paintType, canvasPaths);
                }
            case MotionEvent.ACTION_MOVE:
                MyLog.v(TAG,"MOVING ~~");
                if(pointerCount==2) {
                    pointDraw.drawFlag = 0;
                    MyLog.v(TAG, "TWO POINT MOVING");
                    pointDraw.np1[0] = eventX;
                    pointDraw.np1[1] = eventY;
                    pointDraw.np2[0] = eventX1;
                    pointDraw.np2[1] = eventY1;
                    MyLog.v(TAG,"TWO POINT MOVING:"+ pointDraw.np1[0]+","+pointDraw.np1[1]+","+
                            pointDraw.np2[0]+","+pointDraw.np2[1]+","+
                            Arrays.toString(pointDraw.np1)+","+Arrays.toString(pointDraw.np2));

                    setScale(pointDraw.op1,pointDraw.op2,pointDraw.np1,pointDraw.np2,wigth,heigth);

                }else {
                    if( pointDraw.drawFlag == 1 ) {
                        MyLog.v(TAG, "ONE POINT MOVING------------");
                        movePath(eventX, eventY, canvasPaths);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointDraw.drawFlag = 0;
                MyLog.v(TAG, "MUTI POINTDOWN------------");
                if(pointerCount==2) {
                    pointDraw.op1[0] = eventX;
                    pointDraw.op1[1] = eventY;
                    pointDraw.op2[0] = eventX1;
                    pointDraw.op2[1] = eventY1;
                    MyLog.v(TAG,"TWO_POINTER_DOWN:"+ Arrays.toString(pointDraw.op1)+","+
                            Arrays.toString(pointDraw.op2));
//                    oldDist = spacing(event); //两点按下时的距离
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointDraw.drawFlag = 0;
                if(pointerCount==2) {
                    MyLog.v(TAG, "TWO POINTUP");
                    updateOscale();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                pointDraw.drawFlag = 0;
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = 0f;
        float eventY = 0f;
        float eventX1 = 0f;
        float eventY1 = 0f;
        int pointerCount = event.getPointerCount();
        socketEmitter.sendTouchEvent(event, currentPaintType);
        this.fragment.saveBitmap();
        int eventType = event.getAction() & MotionEvent.ACTION_MASK;
        if(pointerCount==2){
            eventX = event.getX(0);
            eventY = event.getY(0);
            eventX1 = event.getX(1);
            eventY1 = event.getY(1);
        }
        else if(pointerCount == 1){
            eventX = event.getX();
            eventY = event.getY();
        }
        return dealWithTouchEvent(eventType,pointerCount,
                eventX,eventY,eventX1,eventY1,localPointDraw,localPaths,currentPaintType);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float eventX = event.getX();
//        float eventY = event.getY();
//        float wigth = getWidth();
//        float heigth = getHeight();
//        int pointerCount = event.getPointerCount();
//        socketEmitter.sendTouchEvent(event, currentPaintType);
//        this.fragment.saveBitmap();
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
////            case MotionEvent.ACTION_UP:
//////                scale.actionUP(event,pointerCount);
////                MyLog.v(TAG,"ACTION UP--------------- ~~");
////                break;
//            case MotionEvent.ACTION_DOWN:
//                if(pointerCount==2) {
//                    MyLog.v(TAG,"ACTION DOWN ~~");
//                    drawFlag = 0;
//                }
//                else {
////                    testMatrix();
//                    drawFlag = 1;
//                    return startPath(eventX, eventY, currentPaintType, localPaths);
//                }
//            case MotionEvent.ACTION_MOVE:
//                MyLog.v(TAG,"MOVING ~~");
//                if(pointerCount==2) {
//                    drawFlag = 0;
//                    MyLog.v(TAG, "TWO POINT MOVING");
//
//                    np1[0] = event.getX(0);
//                    np1[1] = event.getY(0);
//                    np2[0] = event.getX(1);
//                    np2[1] = event.getY(1);
//                    MyLog.v(TAG,"TWO POINT MOVING:"+ np1[0]+","+np1[1]+","+np2[0]+","+np2[1]+","+
//                            Arrays.toString(np1)+","+Arrays.toString(np2));
//
//                    setScale(op1,op2,np1,np2,wigth,heigth);
////                    for (CanvasPath p : localPaths){
////                        p.setScale(op1,op2,np1,np2,wigth,heigth);
////                    }
////                    for (CanvasPath p : globalPaths){
////                        p.setScale(op1,op2,np1,np2,wigth,heigth);
////                    }
//
//                }else {
//                    if( drawFlag == 1 ) {
//                        MyLog.v(TAG, "ONE POINT MOVING------------");
//                        movePath(eventX, eventY, localPaths);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                drawFlag = 0;
//                MyLog.v(TAG, "MUTI POINTDOWN------------");
//                if(pointerCount==2) {
//                    op1[0] = event.getX(0);
//                    op1[1] = event.getY(0);
//                    op2[0] = event.getX(1);
//                    op2[1] = event.getY(1);
//
//                    MyLog.v(TAG,"TWO_POINTER_DOWN:"+ Arrays.toString(op1)+","+Arrays.toString(op2));
////                    oldDist = spacing(event); //两点按下时的距离
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                drawFlag = 0;
//                if(pointerCount==2) {
//                    MyLog.v(TAG, "TWO POINTUP");
//                    updateOscale();
////                    for (CanvasPath p : localPaths){
////                        p.updateOscale();
////                    }
////                    for (CanvasPath p : globalPaths){
////                        p.updateOscale();
////                    }
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                drawFlag = 0;
//                break;
//            default:
//                return false;
//        }
//        invalidate();
//        return true;
//    }

//    /**
//     * 触摸使用的移动事件
//     *
//     * @param lessX
//     * @param lessY
//     */
//    private void setSelfPivot(float lessX, float lessY) {
//        float setPivotX = 0;
//        float setPivotY = 0;
//        setPivotX = getPivotX() + lessX;
//        setPivotY = getPivotY() + lessY;
//        if (setPivotX < 0 && setPivotY < 0) {
//            setPivotX = 0;
//            setPivotY = 0;
//        } else if (setPivotX > 0 && setPivotY < 0) {
//            setPivotY = 0;
//            if (setPivotX > getWidth()) {
//                setPivotX = getWidth();
//            }
//        } else if (setPivotX < 0 && setPivotY > 0) {
//            setPivotX = 0;
//            if (setPivotY > getHeight()) {
//                setPivotY = getHeight();
//            }
//        } else {
//            if (setPivotX > getWidth()) {
//                setPivotX = getWidth();
//            }
//            if (setPivotY > getHeight()) {
//                setPivotY = getHeight();
//            }
//        }
//        setPivot(setPivotX, setPivotY);
//    }
//
//    /**
//     * 计算两个点的距离
//     *
//     * @param event
//     * @return
//     */
//    private double spacing(MotionEvent event) {
//        if (event.getPointerCount() == 2) {
//            float x = event.getX(0) - event.getX(1);
//            float y = event.getY(0) - event.getY(1);
//            return Math.sqrt(x * x + y * y);
//        } else {
//            return 0;
//        }
//    }
//
//    /**
//     * 平移画面，当画面的宽或高大于屏幕宽高时，调用此方法进行平移
//     *
//     * @param x
//     * @param y
//     */
//    public void setPivot(float x, float y) {
//        setPivotX(x);
//        setPivotY(y);
//    }
//
//
//    /**
//     * 设置放大缩小
//     *
//     * @param scale
//     */
//    public void setScale(float scale) {
//        setScaleX(scale);
//        setScaleY(scale);
//    }
//
//    /**
//     * 初始化比例，也就是原始比例
//     */
//    public void setInitScale() {
//        setScaleX(1.0f);
//        setScaleY(1.0f);
//        setPivot(getWidth() / 2, getHeight() / 2);
//    }
    public void setType(int type) {
        currentPaintType = type;
    }

    public void clear() {
        localPaths.clear();
        globalPaths.clear();
//        localCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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
package com.vinnyoodles.vincent.whiteboardclient;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.vinnyoodles.vincent.whiteboardclient.log.MyLog;

import java.util.Arrays;

/**
 * Created by vincent on 11/7/17.
 */

public class CanvasPath {
    public int paint;
    public Path portrait;
    public Path landscape;
    private Matrix matrix;
//    private float scaleX = 1f;
//    private float scaleY = 1f;
    private float preScale = 1f;//路径设置的缩放，上次缩放后的结果
//    private float curScale = 1f;//前缩放过程的状态
//    private float preOffsetX = 0f;
//    private float preOffsetY = 0f;
//    private float curOffsetX = 0f;
//    private float curOffsetY = 0f;
//    private float[]src1;
//    private float[]src2;
//    private float[]dst1;
//    private float[]dst2;
//    private RectF rectF;

    public Path oPortrait = null;
    public Path oLandscape = null;

    public CanvasPath(int paint) {
        this.paint = paint;
        portrait = new Path();
        landscape = new Path();
        matrix = new Matrix();
//        src1 = new float[2];
//        dst1 = new float[2];
//        src2 = new float[2];
//        dst2 = new float[2];
    }
//    public Matrix getMatrix(){
//        return matrix;
//    }
    public float getScale(){
        return preScale;
    }
//    private float distSpace(float[] p1,float[] p2){
//            float x = p1[0] - p2[0];
//            float y = p1[1] - p2[1];
//            return (float) Math.sqrt(x * x + y * y);
//    }
//    public void setBounds(){
//        portrait.computeBounds(rectF,true);
//        landscape.computeBounds(rectF,true);
//    }
//    public void setScale(float[] op1,float[] op2,float[] np1,float[] np2,float width,float heigth){
////        MyLog.v("Scale","PATH SET SCALE:"+ Arrays.toString(op1)+","+
////                Arrays.toString(op2)+","+Arrays.toString(np1)+","+Arrays.toString(np2));
//        curScale = distSpace(np1,np2)/distSpace(op1,op2);//缩放比例
//
////        matrix.setScale(preScale * curScale,preScale * curScale);
//
//        this.src1[0] = 0f;
//        this.src1[1] = 0f;
//        this.src2[0] = width*1f;
//        this.src2[1] = heigth*1f;
////        RectF re = new RectF();
////        portrait.computeBounds(re,true);
////        this.src1[0] = re.left;
////        this.src1[1] = re.top;
////        this.src2[0] = re.right;
////        this.src2[1] = re.bottom;
//        matrix.mapPoints(dst1,src1);
//        matrix.mapPoints(dst2,src2);
//
////        MyLog.v("Scale","PATH SET SCALE MAPPOINT--11111:"+ Arrays.toString(src1)+","+
////                Arrays.toString(src2)+","+Arrays.toString(dst1)+","+Arrays.toString(dst2));
//
//        curOffsetX = (np1[0]+np2[0])/2 - (op1[0]+op2[0])/2;
//        curOffsetY = (np1[1]+np2[1])/2 - (op1[1]+op2[1])/2;
////        curOffsetX = curOffsetX+(width-dst2[0])/2;
////        curOffsetY = curOffsetY+(heigth-dst2[1])/2;
//
////        matrix.setScale(preScale * curScale,preScale * curScale,
////                (re.left + re.right)/2,(re.top + re.bottom)/2);
////        matrix.setScale(preScale * curScale,preScale * curScale,
////                (dst2[0]+dst1[0])/2,(dst2[1]+dst1[1])/2);
////        matrix.setScale(preScale * curScale,preScale * curScale,
////                width,heigth);
//        matrix.setScale(curScale,curScale,width/2,heigth/2);
////        matrix.setScale(preScale * curScale,preScale * curScale,
////                width-(dst2[0]+dst1[0])/2,heigth-(dst2[1]+dst1[1])/2);
////        matrix.setScale(preScale * curScale,preScale * curScale);
////        MyLog.v("Scale","PATH SET SCALE MAPPOINT--2222:"+ Arrays.toString(src1)+","+
////                Arrays.toString(src2)+","+Arrays.toString(dst1)+","+Arrays.toString(dst2));
////        matrix.postTranslate(curOffsetX+(width-dst2[0])/2,curOffsetY+(heigth-dst2[1])/2);
//        matrix.postTranslate(preOffsetX+curOffsetX,preOffsetY+curOffsetY);
////        matrix.postTranslate((width-dst2[0])/2,(heigth-dst2[1])/2);
//    }
//    public void setScale4(float[] op1,float[] op2,float[] np1,float[] np2,float width,float heigth){
//        MyLog.v("Scale","PATH SET SCALE:"+ Arrays.toString(op1)+","+
//                Arrays.toString(op2)+","+Arrays.toString(np1)+","+Arrays.toString(np2));
//        curScale = distSpace(np1,np2)/distSpace(op1,op2);//缩放比例
//
////        matrix.setScale(preScale * curScale,preScale * curScale);
//
//        this.src1[0] = 0f;
//        this.src1[1] = 0f;
//        this.src2[0] = width*1f;
//        this.src2[1] = heigth*1f;
//
//        MyLog.v("Scale","PATH SET SCALE MAPPOINT--11111:"+ Arrays.toString(src1)+","+
//                Arrays.toString(src2)+","+Arrays.toString(dst1)+","+Arrays.toString(dst2));
//
//        curOffsetX = (np1[0]+np2[0])/2 - (op1[0]+op2[0])/2;
//        curOffsetY = (np1[1]+np2[1])/2 - (op1[1]+op2[1])/2;
////        curOffsetX = curOffsetX+(width-dst2[0])/2;
////        curOffsetY = curOffsetY+(heigth-dst2[1])/2;
//
////        matrix.setScale(preScale * curScale,preScale * curScale,
////                (dst2[0]+dst1[0])/2,(dst2[1]+dst1[1])/2);
////        matrix.setScale(preScale * curScale,preScale * curScale,
////                width/2,heigth/2);
//        matrix.setScale(preScale * curScale,preScale * curScale);
//        MyLog.v("Scale","PATH SET SCALE MAPPOINT--2222:"+ Arrays.toString(src1)+","+
//                Arrays.toString(src2)+","+Arrays.toString(dst1)+","+Arrays.toString(dst2));
////        matrix.postTranslate(curOffsetX+(width-dst2[0])/2,curOffsetY+(heigth-dst2[1])/2);
//        matrix.postTranslate(preOffsetX+curOffsetX,preOffsetY+curOffsetY);
////        matrix.postTranslate((width-dst2[0])/2,(heigth-dst2[1])/2);
//    }
//
//    public void setScale2(float[] op1,float[] op2,float[] np1,float[] np2,float wigth,float heigth){
//        MyLog.v("Scale","PATH SET SCALE:"+ Arrays.toString(op1)+","+
//                Arrays.toString(op2)+","+Arrays.toString(np1)+","+Arrays.toString(np2));
//        curScale = distSpace(np1,np2)/distSpace(op1,op2);//缩放比例
//
////        float opx = (op1[0]+op2[0])/2;
////        float opy = (op1[1]+op2[1])/2;
////        float npx = (np1[0]+np2[0])/2;
////        float npy = (np1[1]+np2[1])/2;
//        curOffsetX = (np1[0]+np2[0])/2 - (op1[0]+op2[0])/2;
//        curOffsetY = (np1[1]+np2[1])/2 - (op1[1]+op2[1])/2;
//        this.src1[0] = 0;
//        this.src1[1] = wigth;
//        this.src2[0] = 0;
//        this.src2[1] = heigth;
//
//        matrix.mapPoints(dst1,src1);
//        matrix.mapPoints(dst2,src2);
//        matrix.reset();
////        matrix.postScale(preScale * curScale,preScale * curScale,wigth/2,heigth/2);
//        matrix.postScale(preScale * curScale,preScale * curScale,
//                (dst2[0]+dst1[0])/2,(dst2[1]+dst1[1])/2);
//        matrix.postTranslate(preOffsetX+curOffsetX,preOffsetY+curOffsetY);
//    }
    public void updateOscale(Matrix matrix){
        if(oPortrait == null){
            oPortrait = new Path(portrait);
        }
        if(oLandscape == null){
            oLandscape = new Path(landscape);
        }
        this.matrix.postConcat(matrix);//后乘，保证变换正确 matrix*this.matrix
        oPortrait.transform(this.matrix,portrait);//防止路径多次变换失真
        oLandscape.transform(this.matrix,landscape);

//        portrait.transform(matrix);
//        landscape.transform(matrix);
//        this.matrix.postConcat(matrix);
    }
//    public void updateOscale() {
////        MyLog.v("myScale", "UUUUUUUUUUUU:" + Arrays.toString(op1) + "," +
////                Arrays.toString(op2) + "," + Arrays.toString(np1) + "," + Arrays.toString(np2));
////        float curScale = distSpace(np1, np2) / distSpace(op1, op2);//缩放比例
//        preScale = preScale * curScale;
////        preOffsetX = preOffsetX + curOffsetX;
////        preOffsetY = preOffsetY + curOffsetY;
//        portrait.transform(matrix);
//        landscape.transform(matrix);
//    }
//    public void setScale1(float[] op1,float[] op2,float[] np1,float[] np2){
//        float odistX = Math.abs(op1[0] - op2[0]);
//        float odistY = Math.abs(op1[1] - op2[1]);
//        float deltX  = Math.abs(np1[0] - np2[0])-Math.abs(op1[0] - op2[0]);
//        float deltY  = Math.abs(np1[1] - np2[1])-Math.abs(op1[1] - op2[1]);
//
////        float scaleX = (odistX+deltX)/odistX;
////        float scaleY = (odistY+deltY)/odistY;
//
////        this.scaleX = scaleX*(odistX+deltX)/odistX;
////        this.scaleY = scaleY*(odistY+deltY)/odistY;
//        this.scaleX = (odistX+deltX)/odistX;
//        this.scaleY = (odistY+deltY)/odistY;
//        this.src1[0] = op1[0];
//        this.src1[1] = op1[1];
//        this.src2[0] = op2[0];
//        this.src2[1] = op2[1];
//        MyLog.v("Scale","XXXXXXXXXXXXXX:"+scaleX+","+scaleY+","+ Arrays.toString(op1)+","+
//                Arrays.toString(op2)+","+Arrays.toString(np1)+","+Arrays.toString(np2));
//        matrix.mapPoints(dst1,src1);
//        matrix.mapPoints(dst2,src2);
//
////        matrix.setScale(scaleX,scaleY);
////        matrix.setScale(0.5f,0.5f);
//        matrix.setScale(scaleX,scaleY,(dst1[0]+dst2[0])/2,(dst1[1]+dst2[1])/2);
//    }
    public void moveTo(float x, float y) {
        portrait.moveTo(x, y);
        landscape.moveTo(y, x);
    }

    public void lineTo(float x, float y) {
        portrait.lineTo(x, y);
        landscape.lineTo(y, x);
    }
}

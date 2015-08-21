package com.example.drawablerotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class RotateableDrawableView extends FrameLayout{

	private Camera mCamera;
	private Matrix mMatrix;
	private ObjectAnimator mAnimator;
	private int mAlpha = 255;
	private float mTheta = 0;
	private float mHeightAbove = -80;
	private float mScale = 1.0f;
	
	private List<RectF> mRectangles = new ArrayList<RectF>();
	private List<Paint> mPaints = new ArrayList<Paint>();
	
	public RotateableDrawableView(Context context) {
		super(context);
		init();
	}
	
	public RotateableDrawableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public RotateableDrawableView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init(){
		
		
		mCamera = new Camera();
		mMatrix = new Matrix();
		
		float width = 600;
		float num = 100;
		float recWidth = width / num;
		float left = 0;
		float right = left + recWidth;
		float top = 0;
		float bottom = 1000;
		Random rand = new Random();
		for(int i = 0; i < num; i++){	
			mRectangles.add(new RectF(left, top, left + width, bottom));
			left += recWidth;
			right += recWidth;
			
			int red = rand.nextInt(255);
			int green = rand.nextInt(255);
			int blue = rand.nextInt(255);
			
			Paint paint = new Paint();
			paint.setDither(true);
			paint.setAntiAlias(true);
			paint.setColor(Color.rgb(red, green, blue));
			paint.setStyle(Style.FILL);
			mPaints.add(paint);
		}
		
		setBackgroundColor(0xFFFFFFFF);
				
		mAnimator = ObjectAnimator.ofFloat(this, "value", 1.0f, 0.0f);
		mAnimator.setRepeatCount(ObjectAnimator.INFINITE);
		mAnimator.setRepeatMode(ObjectAnimator.REVERSE);
		mAnimator.setDuration(1000);	
		mAnimator.start();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("Rot","onDraw drawRect");
				
		for(int i = 0; i < mRectangles.size(); i++){			
						
			mPaints.get(i).setAlpha(mAlpha);
			//canvas.save();
			//canvas.rotate(mTheta + i*2, (mRectangles.get(i).left + mRectangles.get(i).right)/2.0f, (mRectangles.get(i).top + mRectangles.get(i).bottom)/2.0f);
		
			
			
			mCamera.save();
			mCamera.setLocation(0, 0, -mHeightAbove);
			mCamera.rotate(mTheta + i*2, mTheta + i*2, mTheta + i*2);
			mCamera.getMatrix(mMatrix);
			mMatrix.preTranslate(-300, -600);
			mMatrix.postTranslate(300, 600);
			
			canvas.save();
			canvas.concat(mMatrix);	
			canvas.drawRect(mRectangles.get(i), mPaints.get(i));
			canvas.restore();
			
			mCamera.restore();
		}
	}
	
	/*//super.onDraw(canvas);
			float degrees = 45;
			
			for(int i = 0; i < 2000; i++){
				//canvas.save();
				//canvas.rotate(degrees);
				//canvas.skew(0.5f, 1.0f);

				//canvas.drawRect(0.0f, 0.0f, 1000.0f, 1000.0f, mPaint);
				//canvas.restore();
				//degrees += 2;	
				//TODO canvas.
				
				
				canvas.drawRect(0.0f, 0.0f, 1000.0f, 1000.0f, mPaint);
				mCamera.save();
				mCamera.rotate(45, 45, 0);
				mCamera.getMatrix(mMatrix);
				mMatrix.preTranslate(-getWidth()/2, -getHeight()/2);
				mMatrix.postTranslate(getWidth()/2, getHeight()/2);
				canvas.concat(mMatrix);
				//canvas.drawRect(0.0f, 0.0f, 1000.0f, 1000.0f, mPaint);
				//super.onDraw(canvas);
				mCamera.restore();
				
				
			}
			//TODO canvas.skew(, sy);
*/	
	/** Alpha is from 0 to 1.0f */
	public void setValue(float value){
		mAlpha = (int) MathUtils.interpolate(0, 255, value);
		mTheta = MathUtils.interpolate(0, 360, value); 
		mHeightAbove = MathUtils.interpolate(-80, -8, value);
		Log.d("Rot","SetAlpha:" + mAlpha + ",setTheta:" + mTheta + ",mHeightAbove:" + mHeightAbove);
		invalidate();
	}
}

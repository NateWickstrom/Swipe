package com.example.nate.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Draw a bitmap with an x-offset related to touch input.
 */
public class TouchImageView extends View {

    // the picture bitmap
    private Bitmap mBitmap;
    // for filtering touch input
    private boolean mIsTrackingTouch = false;
    // offset from center
    private float mXCenterOffset;
    // boarder zones were we can consider the user's choice (left or right)
    private double xMin, xMax;
    // X center for this view
    private double mCenterX;
    // for drawing current touch locations (debugging)
    private Paint mPaint;
    private float mTouchX;
    // our listener
    private OnSwipeListener mSwipeListener;

    // listener for when touch input crosses into a boarder zone
    public interface OnSwipeListener {
        void onImageSwiped(int xCenter);
    }

    public TouchImageView(Context context) {
        super(context);
        init();
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setOnSwipeListener(OnSwipeListener listener){
        mSwipeListener = listener;
    }

    public void setBitmap(Bitmap bitmap){
        int height = getHeight();
        int width = getWidth();

        mBitmap = bitmap;

        if (width > 0 && height > 0)
            mBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;

        if (!changed || width == 0 || height == 0) return;

        mXCenterOffset = width / 2;

        // boarder zones
        xMax = width * 0.75;
        xMin = width * 0.25;

        // set defaults
        mCenterX = mXCenterOffset;
        mTouchX = mXCenterOffset;

        if (mBitmap != null)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, mXCenterOffset - width/2, 0, null);

        // indicator for touch feedback
        canvas.drawCircle(mTouchX, height/2, 10, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int loc[] = new int[2];
        getLocationInWindow(loc);

        // always get the current touch point for debugging
        mTouchX = event.getRawX() - loc[0];

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Is the touch location valid?
                if (mTouchX > xMax || mTouchX < xMin)
                    break;

                mIsTrackingTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // do we care about this input?
                if (!mIsTrackingTouch)
                    break;

                // did we cross into either boarder zone?
                if (mTouchX > xMax || mTouchX < xMin) {
                    mIsTrackingTouch = false;

                    //reset our x-coordinate
                    mXCenterOffset = (float) mCenterX;

                    // inform listener
                    if (mSwipeListener != null)
                        mSwipeListener.onImageSwiped(Math.round(mTouchX));

                    break;
                }

                // update the bitmap's offset
                mXCenterOffset = mTouchX;
                break;
            case MotionEvent.ACTION_UP:
                mIsTrackingTouch = false;
                break;
        }

        //redraw
        invalidate();
        return true;
    }

}

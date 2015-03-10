package com.example.nate.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * A class to handle the exit animations of view images.
 */
public class AnimateExitImageView extends View {

    // number of pixel to jump each ANIMATION_DELAY
    private static final int RATE = 25;
    // delay between redraws (invalidate())
    private static final long ANIMATION_DELAY = 10;
    // for handler
    private static final int REDRAW_MSG = 0;
    // the picture
    private Bitmap mBitmap;
    // picture offset from views center
    private int mXCenterOffset;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message inputMessage) {
            redraw();
        }
    };

    public AnimateExitImageView(Context context) {
        super(context);
    }

    public AnimateExitImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimateExitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBitmap(Bitmap bitmap, int xCenter){
        mBitmap = bitmap;
        mXCenterOffset = xCenter;

        int height = getHeight();
        int width = getWidth();

        if (width > 0 && height > 0) { //this should always be true, but...
            mBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
            invalidate();
        }

    }

    private void redraw(){
        // calc new offset
        if (mXCenterOffset > getMeasuredWidth() / 2)
            mXCenterOffset += RATE;
        else
            mXCenterOffset -= RATE;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int windowCenterX = getMeasuredWidth()/2;

        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, mXCenterOffset - windowCenterX, 0, null);

        // yuck! todo: changes coordinates to simplify this logic
        if (mXCenterOffset < windowCenterX + getMeasuredWidth() && mXCenterOffset > windowCenterX - getMeasuredWidth() ) {
            // still on the screen
            mHandler.removeMessages(REDRAW_MSG);
            mHandler.sendEmptyMessageDelayed(REDRAW_MSG, ANIMATION_DELAY);
        }
    }
}

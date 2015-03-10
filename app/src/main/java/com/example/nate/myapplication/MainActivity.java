package com.example.nate.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements TouchImageView.OnSwipeListener {

    // views
    private ImageView mImageView;
    private TouchImageView mTouchViewImage;
    private AnimateExitImageView mAnimateExitView;

    // current position in list
    private int pos = 0;

    // list off bitmaps to swipe through
    private final List<Bitmap> mBitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.background);
        mTouchViewImage = (TouchImageView) findViewById(R.id.touchView);
        mAnimateExitView = (AnimateExitImageView) findViewById(R.id.animateView);

        mBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.sunset));
        mBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.mountains));
        mBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.beach));

        mTouchViewImage.setBitmap(mBitmaps.get(0));
        mTouchViewImage.setOnSwipeListener(this);

        BitmapDrawable bd = new BitmapDrawable(getResources(), mBitmaps.get(1));
        mImageView.setImageDrawable(bd);
    }


    @Override
    public void onImageSwiped(int xCenter) {

        // calculate next position in list
        int prevPos = pos;
        pos = ++pos % mBitmaps.size();
        int nextPos = (pos + 1) % mBitmaps.size();

        // set the new image to be swiped
        mTouchViewImage.setBitmap(mBitmaps.get(pos));

        // set the new background image
        BitmapDrawable bd = new BitmapDrawable(getResources(), mBitmaps.get(nextPos));
        mImageView.setImageDrawable(bd);

        // set the exit animation image
        mAnimateExitView.setBitmap(mBitmaps.get(prevPos), xCenter);

    }
}

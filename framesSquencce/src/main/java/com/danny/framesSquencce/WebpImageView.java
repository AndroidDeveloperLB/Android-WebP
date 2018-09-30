package com.danny.framesSquencce;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import static android.content.ContentValues.TAG;

/**
 * this is a ImageView used to display animated WebP files
 *
 * @author danny.jiang
 */

public class WebpImageView extends android.support.v7.widget.AppCompatImageView {
    FrameSequenceDrawable mDrawable;
    // in default, there are only 3 status animations for a WebpImageView
    //private List<FrameSequenceDrawable> drawableList = new ArrayList<>();

    private int defaultCount;

    private CheckingProvider mProvider = new CheckingProvider();

    // stop all webp animation
    public void stop() {
        if (mDrawable != null)
            mDrawable.stop();
    }

    public void destroy() {
        if (mDrawable != null)
            mDrawable.destroy();
        mDrawable = null;
        if (mProvider != null) {
            mProvider.empty();
            mProvider = null;
        }
    }

    public static class CheckingProvider implements FrameSequenceDrawable.BitmapProvider {
        HashSet<Bitmap> mBitmaps = new HashSet<>();

        @Override
        public Bitmap acquireBitmap(int minWidth, int minHeight) {
            //Log.e(TAG, "acquireBitmap here");
            Bitmap bitmap = Bitmap.createBitmap(minWidth, minHeight + 4, Bitmap.Config.ARGB_8888);
            mBitmaps.add(bitmap);
            return bitmap;
        }

        @Override
        public void releaseBitmap(Bitmap bitmap) {
            if (!mBitmaps.contains(bitmap)) throw new IllegalStateException();
            //Log.e(TAG, "need to releaseBitmap here");
            mBitmaps.remove(bitmap);
            bitmap.recycle();
        }

        public boolean isEmpty() {
            return mBitmaps.isEmpty();
        }

        public void empty() {
            if (mBitmaps != null) {
                mBitmaps.clear();
            }
        }
    }

    private OnWebpFinishListener listener;

    /**
     * Listener which used to get if WebP animation has been finished
     */
    public interface OnWebpFinishListener {
        void onAnimationFinished();

        void onAnimationStart();
    }

    public WebpImageView(Context context) {
        super(context);
    }

    public WebpImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // read all attrs for default & neutral & final status
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.webImg);
        defaultCount = mTypedArray.getInteger(R.styleable.webImg_defaultCount, 0);

        // load raw resources into streams, and get FrameSequenceDrawable, store them in array
        mDrawable = initWebpDrawable(mTypedArray.getResourceId(R.styleable.webImg_defaultRawId, -1), defaultCount);
        if (mDrawable != null) {
            mDrawable.setOnAnimationListener(new FrameSequenceDrawable.OnAnimationListener() {
                @Override
                public void onFinished(FrameSequenceDrawable drawable) {
                    if (listener != null)
                        listener.onAnimationFinished();
                }

                @Override
                public void onStart(FrameSequenceDrawable drawable) {
                    if (listener != null)
                        listener.onAnimationStart();
                }
            });
        }
        mTypedArray.recycle();
        setImageDrawable(mDrawable);
    }

    private FrameSequenceDrawable initWebpDrawable(int resId, int animationCount) {
        try {
            InputStream is = getResources().openRawResource(resId);
            return initWebpDrawable(is, animationCount);
        } catch (Exception e) {
            Log.e(TAG, "e is " + e.getMessage());
            return null;
        }
    }

    private FrameSequenceDrawable initWebpDrawable(InputStream is, int animationCount) {
        FrameSequenceDrawable drawable = null;
        FrameSequence fs = null;
        try {
            fs = FrameSequence.decodeByteArray(toByteArray(is));
            fs.setDefaultLoopCount(animationCount);
            drawable = new FrameSequenceDrawable(fs, mProvider);
        } catch (Exception e) {
            Log.e(TAG, "error happens when get FrameSequenceDrawable : "
                    + e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "io not closed in right way : " + e.getMessage());
            }
        }
        return drawable;
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public void setDefaultAnimationCount(int defaultAnimationCount) {
        mDrawable.setAnimationCount(defaultAnimationCount);
    }


    public void setNeutralDrawable(int resId) {
        if (mDrawable != null) {
            mDrawable.destroy();
            mDrawable = null;
        }
        mDrawable = initWebpDrawable(resId, 1);
        mDrawable.setOnAnimationListener(new FrameSequenceDrawable.OnAnimationListener() {
            @Override
            public void onFinished(FrameSequenceDrawable drawable) {
                if (listener != null)
                    listener.onAnimationFinished();
            }

            @Override
            public void onStart(FrameSequenceDrawable drawable) {
                if (listener != null)
                    listener.onAnimationStart();
            }
        });
    }

    public void setNeutralDrawable(InputStream is) {
        if (mDrawable != null) {
            mDrawable.destroy();
            mDrawable = null;
        }
        mDrawable = initWebpDrawable(is, 1);
        mDrawable.setOnAnimationListener(new FrameSequenceDrawable.OnAnimationListener() {
            @Override
            public void onFinished(FrameSequenceDrawable drawable) {
                if (listener != null)
                    listener.onAnimationFinished();
            }
            @Override
            public void onStart(FrameSequenceDrawable drawable) {
                if (listener != null)
                    listener.onAnimationStart();
            }
        });
    }


    /**
     * there are 3 status for every Leading Actor in SubMap
     * <p>
     * DEFAULT:         loop animation
     * WAIT:            displayed every 7 seconds
     * CELEBRATE:       displayed if the activity completed perfectly
     */
    public void playAnimation() {
        if (mDrawable == null)
            return;
        setImageDrawable(mDrawable);
        mDrawable.start();
    }

    public void setFinishedListener(OnWebpFinishListener listener) {
        this.listener = listener;
    }
}

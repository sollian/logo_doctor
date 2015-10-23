package com.sollian.ld.views.otherview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.sollian.ld.R;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.LDUtil;

import java.util.ArrayList;
import java.util.List;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/10/13.
 */
public class LogoFlyView extends ViewGroup {
    private static final int IMG_SIZE_DP = 60;
    private static final float ROTATE_SPEED = 1f;
    private static final float MAX_RADIUS_PERCENT = 0.6f;
    private static final int START_DELAY = 1000;

    private static final int DISPERSE_DURATION = 1000;
    private static final int NULL_DURATION = 5000;
    private static final int MERGE_DURATION = 1500;

    private static final int DEFAULT_LINE_COLOR = Color.WHITE;

    private int maxImgPerFrame = 6;

    private float centerX, centerY;
    private int imgSize;
    private float maxRadius;
    private float curRadiusPercent, curDegree;

    private List<Logo> logos;
    private int curLogoIndex;
    private List<SmartImageView> vImgs;
    private OnClickListener listener;

    private Paint paint;
    private List<Integer> lineColors;

    private FlyState flyState;

    private Handler handler;
    private Runnable rotateRunnable;
    private ValueAnimator animator;

    private List<PointF> pointFs;

    private boolean isExit;

    public LogoFlyView(Context context) {
        super(context);
        init();
    }

    public LogoFlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LogoFlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        imgSize = (int) LDUtil.dp2px(getContext(), IMG_SIZE_DP);

        initList();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

        handler = new Handler();
        rotateRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isExit) {
                    curDegree = curDegree + ROTATE_SPEED > 360 ? 0 : curDegree + ROTATE_SPEED;
                    updateDataAndView();
                    handler.postDelayed(this, 10);
                }
            }
        };
    }

    private void initList() {
        vImgs = new ArrayList<>();
        pointFs = new ArrayList<>();
        lineColors = new ArrayList<>();
        removeAllViews();
        for (int i = 0; i < maxImgPerFrame; i++) {
            SmartImageView v = new SmartImageView(getContext());
            LayoutParams params = new LayoutParams(imgSize, imgSize);
            v.setLayoutParams(params);
            v.setScaleType(ImageView.ScaleType.FIT_XY);
            v.setBackgroundColor(Color.TRANSPARENT);
            v.setOnClickListener(listener);
            vImgs.add(v);
            addView(v);

            pointFs.add(new PointF());
            lineColors.add(DEFAULT_LINE_COLOR);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;

        float maxWH = centerX > centerY ? centerX : centerY;
        maxRadius = maxWH - imgSize / 2f;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (pointFs == null || pointFs.isEmpty()) {
            return;
        }
        for (int i = 0; i < vImgs.size(); i++) {
            View v = vImgs.get(i);

            PointF pointF = pointFs.get(i);
            int left = (int) (pointF.x - imgSize / 2f);
            int right = (int) (pointF.x + imgSize / 2f);
            int top = (int) (pointF.y - imgSize / 2f);
            int bottom = (int) (pointF.y + imgSize / 2f);
            v.layout(left, top, right, bottom);

            v.setScaleX(curRadiusPercent / MAX_RADIUS_PERCENT);
            v.setScaleY(curRadiusPercent / MAX_RADIUS_PERCENT);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (flyState == null) {
            flyState = new DisperseState();
        }
        drawLine(canvas);
        super.dispatchDraw(canvas);
    }

    private void drawLine(Canvas canvas) {
        if (pointFs == null || pointFs.isEmpty()) {
            return;
        }
        for (int i = 0; i < pointFs.size(); i++) {
            paint.setColor(lineColors.get(i));
            canvas.drawLine(centerX, centerY, pointFs.get(i).x, pointFs.get(i).y, paint);
        }
    }

    private void updateDataAndView() {
        float intervalDegree = (float) (2 * Math.PI / pointFs.size());
        for (int i = 0; i < pointFs.size(); i++) {
            double a = curDegree * Math.PI / 180 + intervalDegree * i;
            pointFs.get(i).x = (float) (maxRadius * curRadiusPercent * Math.cos(a) + centerX);
            pointFs.get(i).y = (float) (maxRadius * curRadiusPercent * Math.sin(a) + centerY);
        }
        invalidate();
        requestLayout();
    }

    public void setLogos(List<Logo> logos) {
        this.logos = logos;
        if (logos.size() < maxImgPerFrame) {
            initList();
        }
    }

    public void setLogoClickListener(OnClickListener listener) {
        this.listener = listener;
        if (vImgs != null && !vImgs.isEmpty()) {
            for (View v : vImgs) {
                v.setOnClickListener(listener);
            }
        }
    }

    public void resume() {
        isExit = false;
        flyState = null;
        handler.post(rotateRunnable);
        curRadiusPercent = 0;
    }

    public void pause() {
        isExit = true;
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        flyState = null;
        handler.removeCallbacksAndMessages(null);
    }

    public void cancel() {
        isExit = true;
        handler.removeCallbacksAndMessages(null);
        if (animator != null) {
            animator.cancel();
        }
    }

    private interface FlyState {
    }

    private class DisperseState implements FlyState {

        public DisperseState() {
            animator = ValueAnimator.ofFloat(0, MAX_RADIUS_PERCENT);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(DISPERSE_DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    curRadiusPercent = (Float) animation.getAnimatedValue();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    for (int i = 0; i < vImgs.size(); i++) {
                        SmartImageView v = vImgs.get(i);
                        Logo logo = logos.get((curLogoIndex + i) % logos.size());
                        if (!TextUtils.isEmpty(logo.getWrappedImg())) {
                            v.setImageUrl(logo.getWrappedImg(), R.drawable.ic_launcher, R.drawable.ic_launcher);
                        } else {
                            v.setImageResource(R.drawable.ic_launcher);
                        }
                        v.setTag(logo);
                        if (v.getOnLoadedListener() == null) {
                            v.setOnLoadedListener(new ImageLoadedListener(i));
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isExit) {
                        flyState = new NullState();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    private class NullState implements FlyState {
        public NullState() {
            if (!isExit) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flyState = new MergeState();
                    }
                }, NULL_DURATION);
            }
        }
    }

    private class MergeState implements FlyState {
        public MergeState() {
            animator = ValueAnimator.ofFloat(0, MAX_RADIUS_PERCENT);
            animator.setInterpolator(new OvershootInterpolator(4));
            animator.setDuration(MERGE_DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    curRadiusPercent = (Float) animation.getAnimatedValue();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isExit) {
                        curLogoIndex = curLogoIndex + maxImgPerFrame >= logos.size() ? 0 : curLogoIndex + maxImgPerFrame;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                flyState = new DisperseState();
                            }
                        }, START_DELAY);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.reverse();
        }
    }

    private class ImageLoadedListener implements SmartImageView.OnLoadedListener {
        public int index;

        public ImageLoadedListener(int index) {
            this.index = index;
        }

        @Override
        public void onLoadSuccess(Bitmap bitmap) {
            new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if (swatch == null) {
                        swatch = palette.getMutedSwatch();
                    }
                    lineColors.remove(index);
                    if (null != swatch) {
                        lineColors.add(index, swatch.getRgb());
                    } else {
                        lineColors.add(index, DEFAULT_LINE_COLOR);
                    }
                }
            });
        }

        @Override
        public void onLoadFailed() {
            lineColors.remove(index);
            lineColors.add(index, DEFAULT_LINE_COLOR);
        }
    }

}

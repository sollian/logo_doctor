package com.sollian.ld;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.sollian.ld.svgtools.AnimatedSvgView;
import com.sollian.ld.svgtools.SvgPathUtil;
import com.sollian.ld.utils.LDUtils;

public class LoadActivity extends BaseActivity {

    private TextView vVersion;
    private AnimatedSvgView vAnimatedSvg;
    private float logoOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        init();
    }

    private void init() {
        vVersion = (TextView) findViewById(R.id.version);
        vVersion.setText(LDUtils.getAppVersionName());
        vVersion.setVisibility(View.GONE);

        logoOffset = LDUtils.dp2px(this, 50);

        Button mReset = (Button) findViewById(R.id.reset);
        vAnimatedSvg = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        ViewHelper.setTranslationY(vAnimatedSvg, logoOffset);

        vAnimatedSvg.setGlyphStrings(SvgPathUtil.getSvgPath(this));

        // ARGB values for each glyph
        vAnimatedSvg.setFillPaints(new int[]{210}, new int[]{0},
            new int[]{180}, new int[]{0});

        // 滑动线的颜色
        int traceColor = Color.argb(255, 0, 200, 100);
        int[] traceColors = new int[2]; // 4 glyphs
        // 边缘线的颜色
        int residueColor = Color.argb(80, 175, 190, 6);
        int[] residueColors = new int[2]; // 4 glyphs

        // Every glyph will have the same trace/residue
        for (int i = 0; i < traceColors.length; i++) {
            traceColors[i] = traceColor;
            residueColors[i] = residueColor;
        }
        vAnimatedSvg.setTraceColors(traceColors);
        vAnimatedSvg.setTraceResidueColors(residueColors);

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vAnimatedSvg.reset();
                ViewHelper
                    .setTranslationY(vAnimatedSvg, logoOffset);

                animateLogo();
            }
        });

        vAnimatedSvg
            .setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
                @Override
                public void onStateChange(int state) {
                    if (state == AnimatedSvgView.STATE_FILL_STARTED) {

                        AnimatorSet set = new AnimatorSet();
                        Interpolator interpolator = new DecelerateInterpolator();
                        ObjectAnimator a1 = ObjectAnimator.ofFloat(
                            vAnimatedSvg, "translationY", 0);
                        ObjectAnimator a2 = ObjectAnimator.ofFloat(
                            vVersion, "alpha", 0);
                        a1.setInterpolator(interpolator);
                        set.setDuration(1000).play(a1);
                        set.start();
                    }
                }
            });
    }

    private void animateLogo() {
        vAnimatedSvg.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        vAnimatedSvg.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateLogo();
            }
        }, 1000);
    }

}

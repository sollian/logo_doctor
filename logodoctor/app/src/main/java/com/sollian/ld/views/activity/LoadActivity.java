package com.sollian.ld.views.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.sollian.ld.views.BaseActivity;
import com.sollian.ld.R;
import com.sollian.ld.business.local.LocalManager;
import com.sollian.ld.models.User;
import com.sollian.ld.utils.IntentUtil;
import com.sollian.ld.utils.LDUtil;
import com.sollian.ld.views.svgview.AnimatedSvgView;
import com.sollian.ld.views.svgview.SvgPathUtil;

public class LoadActivity extends BaseActivity {
    private static final int SVG_OFFSET_DP = 50;

    private TextView vVersion;
    private AnimatedSvgView vAnimatedSvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        init();
    }

    private void init() {
        vVersion = (TextView) findViewById(R.id.version);
        vVersion.setText(LDUtil.getAppVersionName());
        vVersion.setVisibility(View.GONE);

        vAnimatedSvg = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        ViewHelper.setTranslationY(vAnimatedSvg, LDUtil.dp2px(this, SVG_OFFSET_DP));

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

        vAnimatedSvg
            .setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
                @Override
                public void onStateChange(int state) {
                    if (state == AnimatedSvgView.STATE_FILL_STARTED) {
                        vVersion.setVisibility(View.VISIBLE);
                        AnimatorSet set = new AnimatorSet();
                        Interpolator interpolator = new DecelerateInterpolator();
                        ObjectAnimator a1 = ObjectAnimator.ofFloat(
                            vAnimatedSvg, "translationY", 0);
                        ObjectAnimator a2 = ObjectAnimator.ofFloat(
                            vVersion, "alpha", 0, 255);
                        a1.setInterpolator(interpolator);
                        set.setDuration(1000).playTogether(a1, a2);
                        set.start();
                        set.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                vAnimatedSvg.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkIfNeedLogin();
                                    }
                                }, 1000);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                    }
                }
            });
    }

    private void checkIfNeedLogin() {
        Intent intent;
        User user = LocalManager.syncGetCurUser();
        if (user == null) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        IntentUtil.startActivity(this, intent);
        finish();
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

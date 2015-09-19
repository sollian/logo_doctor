package com.sollian.ld.views.titlebar;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sollian.ld.R;

/**
 * Created by sollian on 2015/9/15.
 */
public class TitlebarHelper {
    private FrameLayout vRoot;
    private ImageView vLeft;
    private ImageView vRight;
    private TextView vTitle;

    public TitlebarHelper(Activity activity) {
        this(activity.getWindow().getDecorView());
    }

    public TitlebarHelper(View view) {
        vRoot = (FrameLayout) view.findViewById(R.id.titlebar);
        vLeft = (ImageView) view.findViewById(R.id.iv_left);
        vRight = (ImageView) view.findViewById(R.id.iv_right);
        vTitle = (TextView) view.findViewById(R.id.title);
    }

    public void showLeft(boolean visible) {
        vLeft.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showRight(boolean visible) {
        vRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public ImageView getLeft() {
        return vLeft;
    }

    public ImageView getRight() {
        return vRight;
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }

    public void setTitle(int resId) {
        vTitle.setText(resId);
    }
}

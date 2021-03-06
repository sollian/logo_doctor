package smartimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sollian.ld.utils.ThreadUtil;

import java.util.Map;

import smartimageview.SmartImageTask.OnCompleteListener;

public class SmartImageView extends ImageView {
    private static WebImageCache webImageCache;

    private SmartImageTask mCurTask;

    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Helpers to set image by URL
    public void setImageUrl(String url) {
        setImage(new WebImage(url));
    }

    public void setImageUrl(String url, Map<String, String> header) {
        setImage(new WebImage(url), null, null, null, header);
    }

    public void setImageUrl(String url, OnCompleteListener completeListener) {
        setImage(new WebImage(url), completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new WebImage(url), fallbackResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource,
                            OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, completeListener);
    }

    public void setImageUrl(String url, final Integer fallbackResource,
                            final Integer loadingResource) {
        setImage(new WebImage(url), fallbackResource, loadingResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource,
                            final Integer loadingResource, OnCompleteListener completeListener) {
        setImage(new WebImage(url), fallbackResource, loadingResource,
            completeListener);
    }

    // Set image using SmartImage object
    public void setImage(final SmartImage image) {
        setImage(image, null, null, null);
    }

    public void setImage(final SmartImage image,
                         final OnCompleteListener completeListener) {
        setImage(image, null, null, completeListener);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final SmartImage image,
                         final Integer fallbackResource, OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    public void setImage(final SmartImage image,
                         final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }

    public void setImage(final SmartImage image,
                         final Integer fallbackResource, final Integer loadingResource,
                         final OnCompleteListener completeListener) {
        setImage(image, fallbackResource, loadingResource, completeListener,
            null);
    }

    public void setImage(final SmartImage image,
                         final Integer fallbackResource, final Integer loadingResource,
                         final OnCompleteListener completeListener,
                         final Map<String, String> header) {
        // Set a loading resource
        if (loadingResource != null) {
            setImageResource(loadingResource);
        }

        if (header == null) {
            Bitmap bmp = getBmpFromLocale(image.getUrl());
            if (bmp != null) {
                setImageBitmap(bmp);
                if (onLoadedListener != null) {
                    onLoadedListener.onLoadSuccess(bmp);
                }
                return;
            }
        }

        // Cancel any existing tasks for this image view
        if (mCurTask != null) {
            mCurTask.cancel();
            mCurTask = null;
        }

        // Set up the new task
        mCurTask = new SmartImageTask(getContext(), image, header);
        mCurTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null) {
                    setImageBitmap(bitmap);
                    if (onLoadedListener != null) {
                        onLoadedListener.onLoadSuccess(bitmap);
                    }
                } else {
                    // Set fallback resource
                    if (fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                    if (onLoadedListener != null) {
                        onLoadedListener.onLoadFailed();
                    }
                }
                if (completeListener != null) {
                    completeListener.onComplete();
                }
            }
        });

        // Run the task in a threadpool
        ThreadUtil.execute(mCurTask);
    }

    private synchronized Bitmap getBmpFromLocale(String url) {
        if (webImageCache == null) {
            webImageCache = new WebImageCache(getContext());
        }
        return webImageCache.get(url);
    }

    private OnLoadedListener onLoadedListener;

    public void setOnLoadedListener(OnLoadedListener loadedListener) {
        this.onLoadedListener = loadedListener;
    }

    public OnLoadedListener getOnLoadedListener() {
        return onLoadedListener;
    }

    public interface OnLoadedListener {
        void onLoadSuccess(Bitmap bitmap);

        void onLoadFailed();
    }

}

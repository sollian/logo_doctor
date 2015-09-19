
package smartimageview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import com.sollian.ld.utils.HttpManager;
import com.sollian.ld.utils.ImageUtil;

import java.util.Map;

public class WebImage implements SmartImage {

    private static WebImageCache webImageCache;
    private Context mContext;

    private String mUrl;

    public WebImage(String url) {
        this.mUrl = url;
    }

    public Bitmap getBitmap(Context context, Map<String, String> header) {
        mContext = context;
        // Don't leak context
        if (webImageCache == null) {
            webImageCache = new WebImageCache(context);
        }

        // Try getting bitmap from cache first
        Bitmap bitmap = null;
        if (mUrl != null) {
             bitmap = webImageCache.get(mUrl);
            if (bitmap == null) {
                bitmap = getBitmapFromUrl(mUrl, header);
                if (bitmap != null) {
                    webImageCache.put(mUrl, bitmap);
                }
            }
        }
        return bitmap;
    }

    @SuppressLint("NewApi")
    private Bitmap getBitmapFromUrl(String url, Map<String, String> header) {
        Bitmap bitmap = null;
        byte[] data;
        data = HttpManager.getInstance().getHttpByte(mContext, url, header);
        if (data != null) {
            try {
                // 对图像的大小进行处理
                bitmap = ImageUtil.getMaxBmp(data, false);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void removeFromCache(String url) {
        if (webImageCache != null) {
            webImageCache.remove(url);
        }
    }

    @Override
    public String getUrl() {
        return mUrl;
    }
}

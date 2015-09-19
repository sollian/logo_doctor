
package smartimageview;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Map;

public interface SmartImage {
    Bitmap getBitmap(Context context, Map<String, String> header);

    String getUrl();
}

package smartimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sollian.ld.utils.FileUtil;
import com.sollian.ld.utils.ThreadUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public class WebImageCache {
    private ConcurrentHashMap<String, SoftReference<Bitmap>> mMemoryCacheHashMap;
    private String mDiskCachePath;
    private boolean mDiskCacheEnabled = false;

    public WebImageCache(Context context) {
        // Set up in-memory cache store
        mMemoryCacheHashMap = new ConcurrentHashMap<>();
        // Set up disk cache store
        mDiskCachePath = FileUtil.getDirectory(FileUtil.DIR_IMG);
        if (mDiskCachePath != null) {
            File outFile = new File(mDiskCachePath);
            outFile.mkdirs();
            mDiskCacheEnabled = outFile.exists();
        }
    }

    public Bitmap get(final String url) {
        Bitmap bitmap;
        // Check for image in memory
        bitmap = getBitmapFromMemory(url);
        // Check for image on disk cache
        if (bitmap == null) {
            bitmap = getBitmapFromDisk(url);
            // Write bitmap back into memory cache
            if (bitmap != null) {
                cacheBitmapToMemory(url, bitmap);
            }
        }
        return bitmap;
    }

    void put(String url, Bitmap bitmap) {
        cacheBitmapToMemory(url, bitmap);
        cacheBitmapToDisk(url, bitmap);
    }

    void remove(String url) {
        if (url == null) {
            return;
        }
        // Remove from memory cache
        mMemoryCacheHashMap.remove(getCacheKey(url));
        // Remove from file cache
        if (mDiskCachePath != null) {
            File f = new File(mDiskCachePath, getCacheKey(url));
            if (f.exists() && f.isFile()) {
                f.delete();
            }
        }
    }

    public void clear() {
        // Remove everything from memory cache
        mMemoryCacheHashMap.clear();
        // Remove everything from file cache
        if (mDiskCachePath != null) {
            File cachedFileDir = new File(mDiskCachePath);
            if (cachedFileDir.exists() && cachedFileDir.isDirectory()) {
                File[] cachedFiles = cachedFileDir.listFiles();
                for (File f : cachedFiles) {
                    if (f.exists() && f.isFile()) {
                        f.delete();
                    }
                }
            }
        }
    }

    private void cacheBitmapToMemory(final String url, final Bitmap bitmap) {
        mMemoryCacheHashMap.put(getCacheKey(url), new SoftReference<>(
            bitmap));
    }

    private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (mDiskCacheEnabled) {
                    BufferedOutputStream ostream = null;
                    try {
                        File file = new File(mDiskCachePath, getCacheKey(url));
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        ostream = new BufferedOutputStream(
                            new FileOutputStream(file), 2 * 1024);
                        bitmap.compress(FileUtil.BMP_FORMAT,
                            FileUtil.BMP_QUALITY, ostream);
                        ostream.flush();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        FileUtil.close(ostream);
                    }
                }
            }
        });
    }

    private Bitmap getBitmapFromMemory(String url) {
        if (mMemoryCacheHashMap == null) {
            return null;
        }
        Bitmap bitmap = null;
        SoftReference<Bitmap> softRef = null;
        try {
            softRef = mMemoryCacheHashMap.get(getCacheKey(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (softRef != null) {
            bitmap = softRef.get();
        }
        return bitmap;
    }

    private Bitmap getBitmapFromDisk(String url) {
        Bitmap bitmap = null;
        if (mDiskCacheEnabled) {
            String filePath = getFilePath(url);
            File file = new File(filePath);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(filePath);
            }
        }
        return bitmap;
    }

    private String getFilePath(String url) {
        return mDiskCachePath + "/" + getCacheKey(url);
    }

    private String getCacheKey(String url) {
        return FileUtil.getBmpNameFromUrl(url);
    }
}

package com.sollian.ld.utils.http;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by sollian on 2015/9/23.
 */
public class FileUploadAsyncTask extends AsyncTask<File, Integer, String> {

    public interface FileUploadListener {
        void onStart();

        void onProgress(int progress);

        void onFinish(String msg);
    }

    private FileUploadListener listener;

    private String url;
    private Context context;

    public FileUploadAsyncTask(Context context, String url) {
        this(context, url, null);
    }

    public FileUploadAsyncTask(Context context, String url, FileUploadListener listener) {
        this.url = url;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        if (listener != null) {
            listener.onStart();
        }
    }

    @Override
    protected String doInBackground(File... params) {
        // 保存需上传文件信息
//        MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
//        entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        entitys.setCharset(Charset.forName(HTTP.UTF_8));
//
//        File file = params[0];
//        entitys.addPart("file", new FileBody(file));
//        HttpEntity httpEntity = entitys.build();

        MultipartEntity httpEntity = new MultipartEntity(
            HttpMultipartMode.BROWSER_COMPATIBLE, null,
            Charset.forName(HTTP.UTF_8));
        ContentBody cbFile = new FileBody(params[0]);
        httpEntity.addPart("file", cbFile);

        final long totalSize = httpEntity.getContentLength();
        ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(
            httpEntity, new ProgressOutHttpEntity.ProgressListener() {
            @Override
            public void transferred(long transferedBytes) {
                publishProgress((int) (100 * transferedBytes / totalSize));
            }
        });
        return HttpManager.getInstance().postHttp(context, url, progressHttpEntity);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (listener != null) {
            listener.onProgress(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onFinish(result.trim());
        }
    }

}
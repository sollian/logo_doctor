package com.sollian.ld.utils.http;

import android.content.Context;
import android.text.TextUtils;

import com.sollian.ld.custom.ConcurrentHashSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

public class HttpManager {
    public static final String CHARSET = HTTP.UTF_8;

    private static final int POOL_TIMEOUT = 10 * 1000;// 设置连接池超时
    private static final int CONNECT_TIMEOUT = 10 * 1000;
    private static final int READ_TIMEOUT = 10 * 1000;

    private static HttpManager mInstance;
    private ClientConnectionManager mConnMgr = null;
    private HttpParams mParams = null;
    private HttpRequestRetryHandler mRequestRetryHandler = null;
    private ResponseHandler<byte[]> mResponseHandler;

    private ConcurrentHashSet<CustomHttp> mConnSet = new ConcurrentHashSet<>();

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    public String getHttp(Context context, String netUrl) {
        byte[] data = getHttpByte(context, netUrl);
        if (data == null) {
            return null;
        }
        return new String(data);
    }

    public byte[] getHttpByte(Context context, String netUrl) {
        Map<String, String> header = new HashMap<>();
        return getHttpByte(context, netUrl, header);
    }

    public byte[] getHttpByte(Context context, String netUrl,
                              Map<String, String> header) {
        if (TextUtils.isEmpty(netUrl)) {
            return null;
        }
        byte[] result = null;

        HttpGet hg;
        try {
            hg = new HttpGet(netUrl);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        if (header != null && !header.isEmpty()) {
            Set<String> keySet = header.keySet();
            for (String key : keySet) {
                hg.setHeader(key, header.get(key));
            }

        }

        CustomHttp http = new CustomHttp(context, hg);
        mConnSet.add(http);
        try {
            result = getHttpClient().execute(hg, getResponseHandler());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mConnSet.remove(http);
        }
        return result;
    }

    public String postHttp(Context context, String netUrl, HttpEntity entity) {
        Map<String, String> header = new HashMap<>();
        return postHttp(context, netUrl, entity, header);
    }

    public String postHttp(Context context, String netUrl, HttpEntity entity,
                           Map<String, String> header) {
        if (TextUtils.isEmpty(netUrl)) {
            return null;
        }
        String result = null;

        HttpPost hp;
        try {
            hp = new HttpPost(netUrl);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        if (header != null && !header.isEmpty()) {
            Set<String> keySet = header.keySet();
            for (String key : keySet) {
                hp.setHeader(key, header.get(key));
            }

        }

        CustomHttp http = new CustomHttp(context, hp);
        mConnSet.add(http);

        // 把参数设置到httpPost实体
        if (null != entity) {
            hp.setEntity(entity);
        }
        DefaultHttpClient client = getHttpClient();
        client.setHttpRequestRetryHandler(getHttpRequestRetryHandler());
        try {
            byte[] data = client.execute(hp, getResponseHandler());
            if (data == null) {
                result = null;
            } else {
                result = new String(data);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mConnSet.remove(http);
        }

        return result;
    }

    /**
     * 程序退出时调用，调用此函数无需再调用disconnect、disconnectAll函数
     */
    public void release() {
        if (mConnMgr != null) {
            new Thread() {
                @Override
                public void run() {
                    mConnMgr.shutdown();
                    mConnMgr = null;
                }
            }.start();
        }
    }

    /**
     * 中断某一个Context开启的所有网络连接
     *
     * @param context
     */
    public synchronized void disconnect(Context context) {
        if (mConnSet.isEmpty()) {
            return;
        }
        String tag = context.getClass().getSimpleName();
        Set<CustomHttp> temp = new HashSet<>();
        for (CustomHttp http : mConnSet) {
            if (tag.equals(http.getTag())) {
                http.getHttp().abort();
                temp.add(http);
            }
        }
        if (!temp.isEmpty()) {
            mConnSet.removeAll(temp);
        }
    }

    /**
     * 中断所有网络连接
     */
    public synchronized void disconnectAll() {
        if (mConnSet.isEmpty()) {
            return;
        }
        for (CustomHttp http : mConnSet) {
            http.getHttp().abort();
        }
        mConnSet.clear();
    }

    // /**
    // * 将InputStream中的内容读取为比特数组
    // *
    // * @param is
    // * @return
    // */
    // public static byte[] parseStream(InputStream is) {
    // if (is == null) {
    // return null;
    // }
    // byte[] result = new byte[1024];
    //
    // int length = -1;
    // ByteArrayOutputStream byteArrayOutputStream = new
    // ByteArrayOutputStream();
    // try {
    // while ((length = is.read(result)) != -1) {
    // byteArrayOutputStream.write(result, 0, length);
    // }
    // result = byteArrayOutputStream.toByteArray();
    // } catch (IOException e) {
    // Logcat.e(TAG, "parseStream IOException:" + e.getMessage());
    // result = null;
    // } finally {
    // FileManager.close(byteArrayOutputStream);
    // }
    // return result;
    // }

    private HttpRequestRetryHandler getHttpRequestRetryHandler() {
        if (mRequestRetryHandler != null) {
            return mRequestRetryHandler;
        }
        mRequestRetryHandler = new HttpRequestRetryHandler() {
            // 自定义的恢复策略
            public synchronized boolean retryRequest(IOException exception,
                                                     int executionCount, HttpContext context) {
                // 设置恢复策略，在发生异常时候将自动重试3次
                if (executionCount > 3) {
                    // 超过最大次数则不需要重试
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    // 服务停掉则不重新连接
                    return false;
                }
                if (exception instanceof SSLHandshakeException) {
                    // SSL异常不需要重试
                    return false;
                }
                HttpRequest request = (HttpRequest) context
                    .getAttribute(ExecutionContext.HTTP_REQUEST);
                boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
                return !idempotent;
            }
        };
        return mRequestRetryHandler;
    }

    private ResponseHandler<byte[]> getResponseHandler() {
        if (mResponseHandler != null) {
            return mResponseHandler;
        }
        mResponseHandler = new ResponseHandler<byte[]>() {
            public byte[] handleResponse(HttpResponse response)
                throws IOException {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toByteArray(entity);
                } else {
                    return null;
                }
            }
        };
        return mResponseHandler;
    }

    /**
     * 获取HttpClient对象
     *
     * @return
     */
    private DefaultHttpClient getHttpClient() {
        HttpParams httpParams = getHttpParams();
        // 使用线程安全的连接管理来创建HttpClient
        if (mConnMgr == null) {
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                .getSocketFactory(), 443));
            mConnMgr = new ThreadSafeClientConnManager(httpParams, schReg);
        }
        return new DefaultHttpClient(mConnMgr, httpParams);
    }

    private HttpParams getHttpParams() {
        if (mParams != null) {
            return mParams;
        }
        // 设置一些基本参数
        mParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(mParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(mParams, CHARSET);
        HttpProtocolParams.setUseExpectContinue(mParams, true);
        // HttpProtocolParams
        // .setUserAgent(
        // httpParams,
        // "Mozilla/5.0(Linux;U;Android 2.3.3;en-us;Nexus One Build.FRG83) "
        // +
        // "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // // 代理的设置
        // HttpHost proxy = new HttpHost("10.60.8.20", 8080);
        // httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        /* 从连接池中取连接的超时时间 */
        ConnManagerParams.setTimeout(mParams, POOL_TIMEOUT); // 设置连接池超时0秒钟
        HttpConnectionParams.setConnectionTimeout(mParams, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(mParams, READ_TIMEOUT); // 设置等待数据超时时间0秒钟
        return mParams;
    }

    private class CustomHttp {
        private HttpUriRequest mHttp;
        private String mTag;

        public CustomHttp(Context context, HttpUriRequest http) {
            if (context != null) {
                mTag = context.getClass().getSimpleName();
            }
            mHttp = http;
        }

        public HttpUriRequest getHttp() {
            return mHttp;
        }

        public String getTag() {
            return mTag;
        }
    }
}

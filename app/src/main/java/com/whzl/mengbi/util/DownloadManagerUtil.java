package com.whzl.mengbi.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author shaw
 * @date 2018/6/12
 */
public class DownloadManagerUtil {

    private OkHttpClient client;

    public static DownloadManagerUtil getInstance() {
        if (instance == null) {
            synchronized (DownloadManagerUtil.class) {
                if (instance == null) {
                    instance = new DownloadManagerUtil();
                }
            }
        }
        return instance;
    }

    private static DownloadManagerUtil instance;

    public void download(Context context, String url, String fileName, DownLoadListener listener) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() != 200) {
                    listener.onFailed();
                    return;
                }
                InputStream inputStream = response.body().byteStream();
                long totalLength = response.body().contentLength();
                File file = new File(StorageUtil.getDownloadDir(), fileName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    int len = 0;
                    long currSize = 0;
                    byte[] buf = new byte[2048];
                    while ((len = inputStream.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        currSize += len;
                        listener.onProgress((int) (currSize / (float) totalLength * 100 + 0.5));

                    }
                    fos.flush();
                    listener.onSucceed(file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface DownLoadListener {
        void onProgress(int progress);

        void onSucceed(String filePath);

        void onFailed();
    }
}

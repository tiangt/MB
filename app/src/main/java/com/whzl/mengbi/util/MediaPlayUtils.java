package com.whzl.mengbi.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;

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
 * @author nobody
 * @date 2019-12-04
 */
public class MediaPlayUtils {

    private MediaPlayer mediaPlayer;
    private OkHttpClient client;

    public MediaPlayUtils() {
    }

    public static MediaPlayUtils getInstance() {
        if (instance == null) {
            synchronized (MediaPlayUtils.class) {
                if (instance == null) {
                    instance = new MediaPlayUtils();
                }
            }
        }
        return instance;
    }

    private static MediaPlayUtils instance;


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void play(String mp3, String resValue) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/mengbi/mp3/" + resValue);
        if (file.exists()) {
            playByFile(file.getAbsolutePath());
        } else {
            download(mp3, resValue, new DownLoadListener() {
                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onSucceed(String filePath) {
                    playByFile(filePath);
                }

                @Override
                public void onFailed() {

                }
            });
        }
    }

    private void playByFile(String filePath) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            // 设置指定的流媒体地址
            mediaPlayer.setDataSource(filePath);
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);

            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                // 装载完毕 开始播放流媒体
                mediaPlayer.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playByUrl(String url) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            // 设置指定的流媒体地址
            mediaPlayer.setDataSource(url);
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);

            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                // 装载完毕 开始播放流媒体
                mediaPlayer.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String url, String fileName, DownLoadListener listener) {
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

                File parentFile = Environment.getExternalStorageDirectory();
                File download = new File(parentFile.getAbsoluteFile() + "/mengbi/mp3/");
                if (!download.exists()) {
                    download.mkdirs();
                }

                File file = new File(download, fileName);
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

    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

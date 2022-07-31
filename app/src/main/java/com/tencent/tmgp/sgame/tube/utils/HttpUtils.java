package com.tencent.tmgp.sgame.tube.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpUtils {
    private static String result = null;
    private static InputStream b = null;

    public static String sendPost(String url, String param) {
        final CountDownLatch lt = new CountDownLatch(1);
        OkHttpClient okhttp = new OkHttpClient().newBuilder().proxy(Proxy.NO_PROXY).build();
        FormBody.Builder form = new FormBody.Builder();
        if (!param.equals("")) {
            String[] bodyArr = param.split("&");
            for (String item : bodyArr) {
                String[] itemArr = item.split("=");
                if (itemArr.length == 1) {
                    form.add(itemArr[0], "");
                } else {
                    form.add(itemArr[0], itemArr[1]);
                }
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(form.build())
                .build();

        okhttp.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) {
                try {
                    result = Objects.requireNonNull(response.body()).string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lt.countDown();
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {

            }
        });

        try {
            lt.await();
        } catch (InterruptedException ignored) {
        }

        return result;
    }

    /**
     * 阻塞get
     */
    public static InputStream syncGet(String url) {
        final CountDownLatch lt = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) {

                b = Objects.requireNonNull(response.body()).byteStream();
                lt.countDown();

            }
        });
        try {
            lt.await();
        } catch (InterruptedException ignored) {
        }
        return b;
    }

    //异步get
    public static InputStream asyncGet(String url) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) {

                b = Objects.requireNonNull(response.body()).byteStream();

            }
        });
        return b;
    }

    public static void download(String url, String des) {
        //下载json到本地
        InputStream in = HttpUtils.syncGet(url);
        FileUtils.streamToLocal(in, des);
    }
}
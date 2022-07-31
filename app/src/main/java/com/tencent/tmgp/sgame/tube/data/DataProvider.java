package com.tencent.tmgp.sgame.tube.data;

import android.util.Log;

import com.copy.mytebe.R;
import com.tencent.tmgp.sgame.tube.StubApplication;
import com.tencent.tmgp.sgame.tube.helper.ConfigHelper;
import com.tencent.tmgp.sgame.tube.helper.DrawDataHelper;
import com.tencent.tmgp.sgame.tube.utils.FileUtils;
import com.tencent.tmgp.sgame.tube.utils.RandomUtils;
import com.tencent.tmgp.sgame.tube.utils.ShellUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class DataProvider {

    public static String TAG = "mylog";
    //文件
    public static String dir;
    public static String filename;

    //socket
    private static ServerSocket server = null;
    private static Socket client = null;
    public static int timmer=0;
    private static InputStream in;
    private static OutputStream out;
    public static boolean isConnected = false;

    public static void init() {
        dir = FileUtils.getFilesPath() + "/";
        filename = "virsafe";
        File file = new File(dir, filename);
        if (file.exists()) {
            file.delete();
        }
//        FileUtils.copyAssertFile(filename, dir);
        FileUtils.copyFilesFromRaw(StubApplication.getContext(), R.raw.virsafe,"virsafe",FileUtils.getFilesPath());
        ShellUtils.execCommand("chmod 777 " + dir + filename, true);
    }

    public static void runBinary() {
        new Thread() {
            @Override
            public void run() {
                String argv = ConfigHelper.card+" "+ConfigHelper.version;
                String cmd = dir + filename + " "+argv + " >/dev/null 2>&1";
                Log.d("mylog", "cmd:" + cmd);
                ShellUtils.CommandResult result = ShellUtils.execCommand(cmd, true);
                Log.d("mylog", "success:" + result.successMsg + "-----\n" + "err:" + result.errorMsg);
            }
        }.start();
    }

    public static void killBinary() {
        new Thread() {
            @Override
            public void run() {
                ShellUtils.execCommand("pkill -9 " + filename, true);
            }
        }.start();
    }

    public static void setData(String data) {
        DrawDataHelper.setString(data);
    }

    public static void initLocal() {
        runBinary();
    }

    public static void initServer() {
        new Thread(() -> {
            try {
                ConfigHelper.port = RandomUtils.getRandomPort();
                ConfigHelper.save();
                server = new ServerSocket(ConfigHelper.port);
                Log.d(TAG, "端口："+ConfigHelper.port+"\n");
                client = server.accept();
                isConnected =true;
                Log.d(TAG, "连接客户端\n");
                in = client.getInputStream();
                out = client.getOutputStream();
                while (true) {
                    if (in != null) {
                        byte[] buf = new byte[1024];
                        int len = in.read(buf);
                        if (len <= 0) {
                            if (server != null)
                                server.close();
                            DataProvider.setData("");
                            break;
                        } else {
                            byte[] str = new byte[len];
                            System.arraycopy(buf, 0, str, 0, len);
                            String s = new String(str);
//                            Log.d(TAG, "数据"+s+"\n");
                            DataProvider.setData(s);
                        }
                    }
                }
            } catch (IOException e) {
                isConnected = false;
                e.printStackTrace();
            }
        }).start();
    }

    public static void destroyServer() {
        try {
            if (server != null) {
                server.close();
                server = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void closeLocal() {
        if (ConfigHelper.mode.equals("socket"))
            destroyServer();
        killBinary();
    }

    public static void refreshDrawData() {
        DrawDataHelper.setString(FileUtils.readDataFile(".info"));
    }

    public static void Timmer() {
        new Thread(() -> {
            while (true){
                if(timmer==100)timmer=0;
                timmer=timmer+1;
                try {
                    Thread.sleep(8);//毫秒 50 100 185
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}

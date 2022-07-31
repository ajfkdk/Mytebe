package com.tencent.tmgp.sgame.tube.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.tencent.tmgp.sgame.tube.StubApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zl on 15/9/7.
 */
public class FileUtils {

    public static File getTempDir() {
        return StubApplication.getContext().getCacheDir();
    }

    public static File getSelfApk() {
        return new File(StubApplication.getContext().getPackageResourcePath());
    }

    public static String getFilesPath() {
        return StubApplication.getContext().getFilesDir().getAbsolutePath();
    }

    public static void copyFile(final String src, final String dest) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(src).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try {
                if(sourceChannel != null)
                    sourceChannel.close();
                if(destChannel != null)
                    destChannel.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static void ExtractZipFile(String zipName,String src,String dest){
        InputStream is = null;
        OutputStream os = null;
        try {
            ZipFile zipFile = new ZipFile(zipName);
            ZipEntry entry;
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                entry = e.nextElement();
                if(entry.getName().equals(src)){
                    is = new BufferedInputStream(zipFile.getInputStream(entry));
                    os = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = is.read(buffer, 0, buffer.length)) != -1) {
                        os.write(buffer, 0, count);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(is!=null)
                    is.close();
                if(os!=null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void copyAssertFile(String assertName, String dest){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            AssetManager assetManager = StubApplication.getContext().getAssets();
            bis = new BufferedInputStream(assetManager.open(assertName));
            bos = new BufferedOutputStream(new FileOutputStream(dest + "/"+assertName));
            byte[] bys = new byte[1024];
            int len;
            while ((len = bis.read(bys)) != -1) { //读入bys.len长度的数据放入bys中，并返回字节长度
                bos.write(bys, 0, len); //将字节数组bys[0~len]写入
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bis != null)
                    bis.close();
                if(bos != null)
                bos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 复制res/raw中的文件到指定目录
     * @param context 上下文
     * @param id 资源ID
     * @param fileName 文件名
     * @param storagePath 目标文件夹的路径
     */
    public static void copyFilesFromRaw(Context context, int id, String fileName, String storagePath)
    {
        InputStream inputStream=context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists())
        {//如果文件夹不存在，则创建新的文件夹
            file.mkdirs();
        }
        readInputStream(storagePath + File.separator + fileName, inputStream);
    }

    public static void readInputStream(String storagePath, InputStream inputStream)
    {
        File file = new File(storagePath);
        try
        {
            if (!file.exists())
            {
                // 1.建立通道对象
                FileOutputStream fos = new FileOutputStream(file);
                // 2.定义存储空间
                byte[] buffer = new byte[inputStream.available()];
                // 3.开始读文件
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1)
                {// 循环从输入流读取buffer字节
                    // 将Buffer中的数据写到outputStream对象中
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// 刷新缓冲区
                // 4.关闭流
                fos.close();
                inputStream.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String readDataFile(String filename) {
        InputStream is = null;
        String info = null;
        try {
            is = StubApplication.getContext().openFileInput(filename);
            byte[] bys = new byte[1024];
            int len = is.read(bys);
            if (len != -1)
                info = new String(bys, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    public static void streamToLocal(InputStream in,String des){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(new FileOutputStream(des));
            byte[] bys = new byte[1024];
            int len;
            while ((len = bis.read(bys)) != -1) { //读入bys.len长度的数据放入bys中，并返回字节长度
                bos.write(bys, 0, len); //将字节数组bys[0~len]写入
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bis != null)
                    bis.close();
                if(bos != null)
                    bos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String getFileString(String filename) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream buf = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(filename));
            buf = new ByteArrayOutputStream();
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bis != null)
                    bis.close();
                if(buf != null)
                    buf.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return buf.toString();
    }

    public static void writeDataFile(Context context, String filename, String info) {
        OutputStream os = null;
        try {
            os = context.openFileOutput(filename, MODE_PRIVATE);
            os.write(info.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

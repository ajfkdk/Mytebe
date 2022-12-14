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
            while ((len = bis.read(bys)) != -1) { //??????bys.len?????????????????????bys???????????????????????????
                bos.write(bys, 0, len); //???????????????bys[0~len]??????
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
     * ??????res/raw???????????????????????????
     * @param context ?????????
     * @param id ??????ID
     * @param fileName ?????????
     * @param storagePath ????????????????????????
     */
    public static void copyFilesFromRaw(Context context, int id, String fileName, String storagePath)
    {
        InputStream inputStream=context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists())
        {//???????????????????????????????????????????????????
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
                // 1.??????????????????
                FileOutputStream fos = new FileOutputStream(file);
                // 2.??????????????????
                byte[] buffer = new byte[inputStream.available()];
                // 3.???????????????
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1)
                {// ????????????????????????buffer??????
                    // ???Buffer??????????????????outputStream?????????
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// ???????????????
                // 4.?????????
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
            while ((len = bis.read(bys)) != -1) { //??????bys.len?????????????????????bys???????????????????????????
                bos.write(bys, 0, len); //???????????????bys[0~len]??????
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

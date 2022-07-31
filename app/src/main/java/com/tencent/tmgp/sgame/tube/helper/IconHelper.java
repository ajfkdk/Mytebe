package com.tencent.tmgp.sgame.tube.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.alibaba.fastjson.JSONArray;
import com.tencent.tmgp.sgame.tube.data.HeroEntity;
import com.tencent.tmgp.sgame.tube.utils.FileUtils;
import com.tencent.tmgp.sgame.tube.utils.HttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

public class IconHelper {
    private static int maxCache = 20;
    public static HashMap<Integer, Bitmap> iconCache = new HashMap<>();
    public static HashMap<Integer, String> nameCache = new HashMap<>();

    public static float radius = 17f;

    //下载头像到本地（不加载到内存，用到时加载）
    //缓存id-name
    public static void init() {
        String url = "https://pvp.qq.com/web201605/js/herolist.json";
        String des = FileUtils.getFilesPath() + "/json";
        HttpUtils.download(url, des);
        String jsonStr = FileUtils.getFileString(des);
        new Thread(()-> {
            try {
                List<HeroEntity> hero_list = JSONArray.parseArray(jsonStr, HeroEntity.class);
                int id;
                String name;
                for (HeroEntity hero : hero_list) {
                    id = hero.getId();
                    name = hero.getName();
                    //缓存id-name
                    nameCache.put(id, name);
                    if (!new File(FileUtils.getFilesPath() + "/" + hero.getId()).exists()) {
                        //下载头像
                        downHeadById(id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    //提供外部调用获取HeroIcon （缓存）
    public static Bitmap getHeroIcon(int id) {
        if (!iconCache.containsKey(id)) {
            Bitmap heroBitmap=getImageBitmap(id);

            if(heroBitmap==null) return heroBitmap;
            iconCache.put(id, heroBitmap);
        }
        //清理缓存
        if (iconCache.size() > maxCache)
            iconCache.clear();
        return iconCache.get(id);
    }

    //提供外部调用获取HeroName
    public static String getHeroName(int id) {
        return nameCache.get(id);
    }

    //本地文件流 -> 内存bitmap
    private static Bitmap getImageBitmap(int id) {
        String filename = FileUtils.getFilesPath() + "/" + id;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        return bitmapScale(bitmap, radius * 2, radius * 2);
    }

    //方形bitmap -> 圆形bitmap
    private static Bitmap bitmapScale(Bitmap bitmap, float dst_w, float dst_h) {
        Bitmap circleBitmap = Bitmap.createBitmap((int) dst_w, (int) dst_w, Bitmap.Config.ARGB_8888);
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = dst_w / src_w;
        float scale_h = dst_h / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);
        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(dst_w / 2, dst_w / 2, dst_w / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(dstbmp, 0, 0, paint);
        return circleBitmap;
    }


    //下载头像
    private static void downHeadById(int id) {
        String url = "https://game.gtimg.cn/images/yxzj/img201606/heroimg/" + id + "/" + id + ".jpg";
        String des = FileUtils.getFilesPath() + "/" + id;
        FileUtils.streamToLocal(HttpUtils.syncGet(url), des);
    }
}

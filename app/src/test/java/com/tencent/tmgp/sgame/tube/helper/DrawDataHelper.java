package com.tencent.tmgp.sgame.tube.helper;
import com.tencent.tmgp.sgame.tube.data.DrawData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DrawDataHelper {
    public static Queue<String> history = new LinkedList<>();
    private static String string;
    private static String cache;
    public static List<Integer> buff_data;

    public static void setString(String string) {
        if (string != null && !string.equals("")) {
            DrawDataHelper.string = string;
            history.offer(string);
            if (history.size() >= 10) {
                history.poll();
            }
        }
    }

    public static String getString() {
        if (string == null || string.equals("")) {
            string = cache;
        } else {
            cache = string;
        }
        return string;
    }

    public static List<DrawData> readData() {
        if (string == null || string.equals("")) {
            string = cache;
        } else {
            cache = string;
        }
//        Log.d("readData", string);
        return parseData(string);
    }

    public static List<DrawData> parseData(String text) {
        List<DrawData> result = new ArrayList<>();
        try {
            String buff = text.substring(text.lastIndexOf('|') + 1);
            buff_data = new ArrayList<>();
            String[] b = buff.split(";");
            for (int i = 0; i < 4; i++) {
                buff_data.add(Integer.parseInt(b[i]) / 1000);
            }
        } catch (Exception ignore) {

        }
        if(text == null)
            return result;
        String[] datas = text.trim().split(";"); //读取数据并分割
        for (int i = 0; i < 5; i++) {
            String data = datas[i];
            String[] infos = data.split("&");
//            System.out.println(Arrays.toString(infos));
            //d.HeroID, d.CampID, d.MapX, d.MapY, d.PercentBlood, d.RectX, d.RectY
            DrawData drawData = new DrawData();
            try {
                drawData.HeroID = Integer.parseInt(infos[0]);
                ConfigHelper.camp = Integer.parseInt(infos[1]);
                int locX = Integer.parseInt(infos[2]);
                int locY = Integer.parseInt(infos[3]);
                if (ConfigHelper.camp == 2) {
                    drawData.MapX = (int) (0.00332 * locX + 150);
                    drawData.MapY = (int) (-0.00332 * locY + 150);
                } else {
                    drawData.MapX = (int) (-0.00332 * locX + 150);
                    drawData.MapY = (int) (0.00332 * locY + 150);
                }
                drawData.PercentBlood = Float.parseFloat(infos[4]);
                drawData.CdD = Integer.parseInt(infos[5]);
                drawData.CdZ = Integer.parseInt(infos[6]);
                drawData.RectX = Integer.parseInt(infos[7]);
                drawData.RectY = Integer.parseInt(infos[8]);
                drawData.HHH = Integer.parseInt(infos[9]);
                drawData.Hc = Integer.parseInt(infos[10]);
                result.add(drawData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getCache() {
        return cache;
    }

    public static void setCache(String cache) {
        DrawDataHelper.cache = cache;
    }
}


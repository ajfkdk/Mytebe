package com.tencent.tmgp.sgame.tube.data;

public class DrawData {
    public int HeroID;
    public int MapX;
    public int MapY;
    public float PercentBlood;
    public int CdD;
    public int CdZ;
    public int RectX;
    public int RectY;
    public int HHH;
    public int Hc;

    @Override
    public String toString() {
        return "DrawData{" +
                "HeroID=" + HeroID +
                ", MapX=" + MapX +
                ", MapY=" + MapY +
                ", PercentBlood=" + PercentBlood +
                ", RectX=" + RectX +
                ", RectY=" + RectY +
                ", HHH=" + HHH +
                ", Hc=" + Hc +
                '}';
    }
}

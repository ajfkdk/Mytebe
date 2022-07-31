package com.tencent.tmgp.sgame.tube.data;


import com.alibaba.fastjson.annotation.JSONField;

public class HeroEntity {
    public HeroEntity() {
    }

    public HeroEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JSONField(name="ename")
    private int id;


    @JSONField(name="cname")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}

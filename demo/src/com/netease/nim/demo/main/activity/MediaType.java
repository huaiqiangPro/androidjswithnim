package com.netease.nim.demo.main.activity;



public enum MediaType {

    JPG("jpg"), GIF("gif"), VIDEO("mp4");

    private String name;

    MediaType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

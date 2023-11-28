package com.vinnyoodles.vincent.whiteboardclient.utils;

public class MsgEvent {
    private String msg;
    private String Tag;

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
    public MsgEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}



package com.changsdev.whoaressuproject.model;

import java.sql.Timestamp;

public class ChatInfo { //아이템정보 입력할때 쓰는 클래스임.
    public String sender;
    public String text;
    public String roomuid;
    public String oppositename;

    public ChatInfo(String sender, String text,String roomuid,String oppositename){
        this.sender = sender;
        this.text = text;
        this.roomuid=roomuid;
        this.oppositename=oppositename;
    }
}

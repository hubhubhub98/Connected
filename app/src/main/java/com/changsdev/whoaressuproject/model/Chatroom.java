package com.changsdev.whoaressuproject.model;

import java.sql.Time;
import java.sql.Timestamp;

public class Chatroom {
    public String oppositeUID;
    public String oppositeusername;
    public String roomuid;

    public Chatroom(String oppositeUID,String oppositeusername,String roomuid) {
        this.oppositeUID = oppositeUID;
        this.oppositeusername=oppositeusername;
        this.roomuid=roomuid;
    }
}

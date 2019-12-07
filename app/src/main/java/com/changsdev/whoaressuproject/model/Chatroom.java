package com.changsdev.whoaressuproject.model;

import java.sql.Time;
import java.sql.Timestamp;

public class Chatroom {
    public String oppositeUID;
    public String oppositeusername;

    public Chatroom(String oppositeUID, String oppositeusername) {

        this.oppositeUID = oppositeUID;
        this.oppositeusername=oppositeusername;
    }
}

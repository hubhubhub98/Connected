package com.changsdev.whoaressuproject.model;

public class UserVO {
    private String uid; //유저들을 구분짓는 식별자, 파이어베이스가 알아서 배정해줌
    private String userName; //유저의 이름

    public UserVO(){

    }

    public UserVO(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}

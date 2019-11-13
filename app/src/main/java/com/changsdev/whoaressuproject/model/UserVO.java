package com.changsdev.whoaressuproject.model;

public class UserVO {
    private String uid; //유저들을 구분짓는 식별자, 파이어베이스가 알아서 배정해줌
    private String userName; //유저의 이름
    private String userPhoneNumber; //유저의 폰번호. 조직만 해당
    private String userEmail;
    private boolean isOrg; // 유저가 조직인지 아닌지 확인

    public UserVO(){

    }

    public UserVO(String uid, String userName) {
        this.uid = uid;
        this.userName = userName;
    }

    public UserVO(String uid, String userName, String userEmail){
        this.uid = uid;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public UserVO(String uid, String userName, String userPhoneNumber, boolean isOrg){
        this.uid = uid;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.isOrg = isOrg;
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

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public boolean isOrg() {
        return isOrg;
    }

    public void setOrg(boolean org) {
        isOrg = org;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

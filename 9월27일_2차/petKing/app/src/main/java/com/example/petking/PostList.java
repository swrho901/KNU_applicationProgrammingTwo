package com.example.petking;

public class PostList {
    private String profile;
    private String title;
    private String currentStat;
    private String address;
    private String main_context;
    private int likeNum;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpinnerText() {
        return currentStat;
    }

    public void setSpinnerText(String currentStat) {
        this.currentStat = currentStat;
    }

    public String getUserAd() {
        return address;
    }

    public void setUserAd(String address) { this.address = address; }

    public String getCont() {
        return main_context;
    }

    public void setCont(String main_context) {
        this.main_context = main_context;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

}

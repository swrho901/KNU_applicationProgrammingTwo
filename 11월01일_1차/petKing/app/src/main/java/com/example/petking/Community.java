package com.example.petking;

public class Community {
    int image;
    String title; // 제목
    String main_context; // 주요 내용 (긴 내용)

    public int like_number; // 좋아요 갯수
    public String id;
    //public String id;

    public Community(int image, String title, String main_context, int like_number, String id) { //, String id
        this.image = image;
        this.title = title;
        this.main_context = main_context;
        this.like_number = like_number;
        this.id = id;
    }

    public String getComTitle() { return title; }

    public String getComContent() { return main_context; }

    public int getComImage() { return image; }

    public int getComLike_number() {
        return like_number;
    }

    public String getComId() {
        return id;
    }
}

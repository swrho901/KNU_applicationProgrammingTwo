package com.example.petking;

public class Community {

    public String title; // 제목
    public String main_context; // 주요 내용 (긴 내용)

    public int like_number; // 좋아요 갯수
    public String id;
    //public String id;

    public Community() {
    }

    public Community(String title, String main_context, int like_number, String id) { //, String id
        this.title = title;
        this.main_context = main_context;
        this.like_number = like_number;
        this.id = id;

    }
}

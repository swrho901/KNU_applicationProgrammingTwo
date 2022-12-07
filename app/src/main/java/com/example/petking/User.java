package com.example.petking;

public class User {
     public String name;
     public String email;
     public String age;
     public String sex;
     public String id;
     public String address;
     public double rating;
     public int rating_num;
     public int context_num; // 글쓴 index 저장 하는 공간
     public int text_num; // 쪽지 받은 index 저장 하는 공간

     public User(){}

     public User(String name, String email, String id, String age,
                 String sex, String address, double rating, int rating_num,
                 int context_num, int text_num){
          this.age = age;
          this.name = name;
          this.sex = sex;
          this.email = email;
          this.id = id;
          this.address = address;
          this.rating = rating;
          this.rating_num = rating_num;
          this.context_num = context_num;
          this.text_num = text_num;
     }
}
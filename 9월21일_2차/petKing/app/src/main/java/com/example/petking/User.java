package com.example.petking;

public class User {
     public String name;
     public String email;
     public String age;
     public String sex;
     public String id;
     public double rating;
     public int rating_num;

     public User(){}

    public User(String name, String email, String id, String age,
                String sex, double rating, int rating_num){
         this.age = age;
         this.name = name;
         this.sex = sex;
         this.email = email;
         this.id = id;
         this.rating = rating;
         this.rating_num = rating_num;
    }
}
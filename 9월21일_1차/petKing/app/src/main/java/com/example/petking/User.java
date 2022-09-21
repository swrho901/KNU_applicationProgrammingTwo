package com.example.petking;

public class User {
     public String name;
     public String email;
     public String age;
     public String sex;

     public User(){}

    public User(String name, String email, String age, String sex){
         this.age = age;
         this.name = name;
         this.sex = sex;
         this.email = email;
    }
}
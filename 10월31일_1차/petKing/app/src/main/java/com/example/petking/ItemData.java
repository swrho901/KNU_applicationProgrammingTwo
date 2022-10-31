package com.example.petking;

public class ItemData {
    int image;
    String title;
    String content; // 바꾸기 귀찮으니 이걸그냥 모집중으로 생각하자
    int money;
    String address;

    public ItemData(int image, String title, String content, int money, String address) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.money = money;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAddress(){return address;}

    public void setAddress(String address){this.address = address;}

    public int getMoney(){return money;}

    public void setMoney(int money){
        this.money = money;
    }
}

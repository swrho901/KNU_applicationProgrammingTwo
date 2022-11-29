package com.example.petking;

public class Context {
    //이미지는 realtime Database 가 아니라 firebase Storage 에 업로드해야함
    //일단 이미지는 여기 안넣음

    public int total_read_num;
    public String title;
    public String currentStat; // 모집중 / 모집완료
    public String address; // 간단한 주소 ex OO시 OO동
    public String main_context; // 주요 내용 (긴 내용)
    // 이거는 리스트 뷰에 띄우는거 말고 리스트뷰 클릭했을 때 띄우는 걸로 하자

    public String typeOfContext; // 어떤종류의 글인지
    public int money;
    public int like_number; // 좋아요 갯수
    public String id;
    public int negotiation; // 네고가능 여부  1 : 가능 , 0 : 불가능, 디폴트는 네고불가능으로 해둠

    public Context(){}

    public Context(int total_read_num, String title, String currentStat, String address,
                   String main_context, String typeOfContext, int money,
                   int like_number, String id, int negotiation){
        this.total_read_num = total_read_num;
        this.title = title;
        this.currentStat = currentStat;
        this.address = address;
        this.main_context = main_context;
        this.typeOfContext = typeOfContext;
        this.money = money;
        this.like_number = like_number;
        this.id = id;
        this.negotiation = negotiation;

    }


}

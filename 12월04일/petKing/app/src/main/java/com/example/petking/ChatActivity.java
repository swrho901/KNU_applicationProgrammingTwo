package com.example.petking;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    MyAdapter  adapter;
    private RecyclerView.LayoutManager layoutManager; // 리사이클러뷰의 레이아웃을 정해줄 레이아웃 매니저
    FirebaseDatabase database;
    ArrayList<Chat> chatArrayList; // 모델(이메일과 텍스트)을 나열함

    EditText edt;
    Button btnSend;
    String stEmail, my_id, combineId, combineId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatArrayList = new ArrayList<>(); // Chat 모델을 나열하는 ArrayList

        database = FirebaseDatabase.getInstance();

        stEmail = getIntent().getStringExtra("email"); // 인텐트로 MainActivity에서 입력된 이메일 값을 받아오는 문자열 값 email
        my_id = getIntent().getStringExtra("my_id");


        edt = (EditText)findViewById(R.id.edt);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true); // 변경하지 않음 -> 항목의 높이가 바뀌지 않아야 비용이 적게 드므로 성능이 좋음

        layoutManager = new LinearLayoutManager(this); // 리사이클러뷰의 레이아웃을 정해줄 레이아웃 매니저
        recyclerView.setLayoutManager(layoutManager); // 리사이클러뷰에 리니어 레이아웃 매니저를 사용함

        String[] myDataset = {"테스트1", "테스트2","테스트3", "테스트4"}; // myDataset에 다음과 같은 문자열 삽입
        adapter = new MyAdapter(chatArrayList, stEmail); // chatArrayList를 어댑터로 연결, 회원의 이메일도 넘김
        recyclerView.setAdapter(adapter); // 리사이클뷰에 어댑터를 설정

        // id 결합
        combineId = my_id + stEmail;
        combineId2 = stEmail + my_id;


        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() { // 메시지 전송 버튼(화살표)을 누르면
            @Override
            public void onClick(View v) {
                String stText = edt.getText().toString(); // 입력창에 입력한 문자가 stText에 담겨짐
                edt.setText(""); // 입력창은 공백이됨

                Calendar calendar = Calendar.getInstance(); // 캘린더 객체 인스턴스 calendar
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MMM HH:mm:ss"); // SimpleDataFormat 이라는 날짜와 시간을 출력하는 객체 생성, hh을 HH로 변경했더니 24시각제로 바뀜
                String datetime = dateFormat.format(calendar.getTime()); // 캘린더 날짜시간 값을 가져와서 문자열인 datatime 으로 변환함
                System.out.println(datetime); // 문자열 datetime 값 출력

                DatabaseReference myRef = database.getReference("message").child(combineId).child(datetime); // 데이터베이스에 message 안에 자식으로 날짜인 datatime이 들어감

                Hashtable<String, String> numbers // 해시테이블 객체 numbers 생성 후 안에 이메일과 입력한 문자가 들어감
                        = new Hashtable<String, String>();
                numbers.put("email", stEmail);
                numbers.put("text", stText);

                myRef.setValue(numbers); // DB 객체에 numbers 객체가 설정됨

                DatabaseReference myRef2 = database.getReference("message").child(combineId2).child(datetime); // 데이터베이스에 message 안에 자식으로 날짜인 datatime이 들어감

                Hashtable<String, String> numbers2 // 해시테이블 객체 numbers 생성 후 안에 이메일과 입력한 문자가 들어감
                        = new Hashtable<String, String>();
                numbers2.put("email", stEmail);
                numbers2.put("text", stText);

                myRef2.setValue(numbers); // DB 객체에 numbers 객체가 설정됨
            }
        });

        ChildEventListener childEventListener = new ChildEventListener() { // 현재 시간의 자식들인 이메일, 텍스트들을 담는 객체 생성
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) { // 현재 시간의 자식들을 추가함
                Log.d(TAG, "child를 추가합니다. :" + dataSnapshot.getKey());

                // 리스너는 이벤트 발생 시점에 DB에서 지정된 위치에 있던 데이터를 포함하는
                // DataSnapShot을 수신함. getValue()를 호출하면서 데이터의 자바 객체 표현이 반환됨


                Chat chat = dataSnapshot.getValue(Chat.class);


                // 모델인 Chat.class 에서 이메일이랑 텍스트를 가져와서 확인
                String stEmail = chat.getEmail();
                String stText = chat.getText();
                Log.d(TAG, "이메일 가져오기 : " + stEmail);
                Log.d(TAG, "텍스트 가져오기 : " + stText);

                chatArrayList.add(chat); // Chat 모델을 추가하고
                adapter.notifyDataSetChanged(); // 변경된다는 것을 어댑터에게 알려줌
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "Chile가 변경되었습니다. :" + dataSnapshot.getKey());
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "CHild가 삭제되었습니다. :" + dataSnapshot.getKey());
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "Child가 이동되었습니다. :" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "취소되었습니다. ", databaseError.toException());
            }
        };


        DatabaseReference databaseReference;
        databaseReference = database.getReference("message").child(combineId2);
        databaseReference.addChildEventListener(childEventListener);
    }
}

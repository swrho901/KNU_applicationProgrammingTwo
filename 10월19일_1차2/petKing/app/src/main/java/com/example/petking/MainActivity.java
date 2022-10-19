package com.example.petking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerAdapter.MyRecyclerViewClickListener {

    ArrayList<ItemData> dataList = new ArrayList<>();
    int[] cat = {R.drawable.doggy};

    final MyRecyclerAdapter adapter = new MyRecyclerAdapter(dataList);
    static int i=0;

    private boolean fabMain_status = false;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabEdit;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    public int total_num_of_contents = 4;

    public Context c;
    public Context[] Carr = new Context[10000];
    public String[] addArr = new String[10000];
    public String[] contextArr = new String[10000];
    public String[] typeOfContextArr = new String[10000];
    public String[] idArr = new String[10000];
    public String[] currentStatArr = new String[10000];
    public int[] likeNumArr = new int[10000];
    public int[] moneyArr = new int[10000];
    public int[] negoArr = new int[10000];
    public String[] titleArr = new String[10000];


    public int contextArrIdx = 0;

    public String user_address, user_id;
    public Uri tempUri;
    ImageView load;
    public int totalContextNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 정보받아오고 리사이클러뷰 업데이트 해야 해서 일부러 밑에다가 놔둠
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // 리싸이클러뷰 구분선
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        // 구분선 추가
        recyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        for (int i=0; i<1; i++) { // 이부분 더 만져야함 //

/*
            dataList.add(new ItemData(cat[i], "TITLE "+i,
                    "dd", 12000, "복현동"));
*/


        }

        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);


        // 스토리지 불러오는것 테스트 용 코드
        load = (ImageView)findViewById(R.id.testimg);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("photo");
        if (pathReference == null) {
            Toast.makeText(MainActivity.this, "저장소에 사진이 없습니다." ,Toast.LENGTH_SHORT).show();
        } else {
            StorageReference submitProfile = storageReference.child("969833");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MainActivity.this).load(uri).into(load);
                    tempUri = uri;

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        fabMain = findViewById(R.id.fabMain);
        fabCamera = findViewById(R.id.fabCamera);
        fabEdit = findViewById(R.id.fabEdit);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Button btnLogout = findViewById(R.id.main_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        TextView tvName = findViewById(R.id.tvName);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec spec;

        // 탭에 넣을 이미지 설정
        ImageView tabwidget01 = new ImageView(this);
        tabwidget01.setImageResource(R.drawable.ic_baseline_main_24);

        ImageView tabwidget02 = new ImageView(this);
        tabwidget02.setImageResource(R.drawable.ic_baseline_chat_24);

        ImageView tabwidget03 = new ImageView(this);
        tabwidget03.setImageResource(R.drawable.ic_baseline_comm_24);

        ImageView tabwidget04 = new ImageView(this);
        tabwidget04.setImageResource(R.drawable.ic_baseline_my_info_24);

        //탭에 이미지 생성한 것 넣어주기
        TabHost.TabSpec tabMain = tabHost.newTabSpec("main").setIndicator(tabwidget01);
        tabMain.setContent(R.id.main);
        tabHost.addTab(tabMain);

        TabHost.TabSpec tabChat = tabHost.newTabSpec("chat").setIndicator(tabwidget02);
        tabChat.setContent(R.id.chat);
        tabHost.addTab(tabChat);

        TabHost.TabSpec tabComm = tabHost.newTabSpec("comm").setIndicator(tabwidget03);
        tabComm.setContent(R.id.comm);
        tabHost.addTab(tabComm);

        TabHost.TabSpec tabMyinfo = tabHost.newTabSpec("myinfo").setIndicator(tabwidget04);
        tabMyinfo.setContent(R.id.myinfo);
        tabHost.addTab(tabMyinfo);


        //이거는 테스트용임
/*        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("context").child("7865").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               *//* c = snapshot.getValue(Context.class);
                String addd = c.address.substring(10,17);
                dataList.add(new ItemData(cat[i], c.title,
                        c.typeOfContext, c.money, addd));*//*
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    tvName.setText(user.name);
                    user_id = user.id;
                    user_address = user.address;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference mFirebaseRef = database.getReference("context");
        int[] visit = new int[1000000];
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                for (DataSnapshot poly : snapshot.getChildren()) {

                    String kk = snapshot.getKey();
                    int v = Integer.parseInt(kk);
                    if(visit[v] == 1) continue;
                    visit[v] = 1;



                    String ididid = snapshot.child("id").getValue(String.class);
                    String adadad = snapshot.child("address").getValue(String.class);
                    String tititi = snapshot.child("title").getValue(String.class);
                    String currentStst = snapshot.child("currentStat").getValue(String.class);
                    int likenunu = snapshot.child("like_number").getValue(Integer.class);
                    int moneymoney = snapshot.child("money").getValue(Integer.class);
                    int negonego = snapshot.child("negotiation").getValue(Integer.class);
                    String main_ctxctx = snapshot.child("main_context").getValue(String.class);
                    String toctoc = snapshot.child("typeOfContext").getValue(String.class);
                    //String toctoc = snapshot.child("typeOfContext").getValue(String.class);

                    addArr[contextArrIdx] = adadad; //.substring(10,17) //
                    titleArr[contextArrIdx] = tititi;
                    moneyArr[contextArrIdx] = moneymoney;
                    typeOfContextArr[contextArrIdx] = toctoc;
                    idArr[contextArrIdx] = ididid;
                    currentStatArr[contextArrIdx] = currentStst;
                    likeNumArr[contextArrIdx] = likenunu;
                    negoArr[contextArrIdx] = negonego;
                    contextArr[contextArrIdx] = main_ctxctx;
                    contextArrIdx++;

                    //일단 4개만 테스트

                    /*Carr[contextArrIdx].address = adadad;
                    Carr[contextArrIdx].main_context = main_ctxctx;
                    Carr[contextArrIdx].typeOfContext = toctoc;
                    Carr[contextArrIdx].id = ididid;
                    Carr[contextArrIdx].currentStat = currentStst;
                    Carr[contextArrIdx].like_number = likenunu;
                    Carr[contextArrIdx].money = moneymoney;
                    Carr[contextArrIdx].negotiation = negonego;
                    Carr[contextArrIdx].title = tititi;

                    contextArrIdx++;*/

                    //String aa = poly.getValue(Context.class).address;
                    String strKey = poly.getKey();
                    String id=String.valueOf(poly.child(strKey).child("id").getValue());
                    String title=String.valueOf(poly.child("title").getValue());
                    int k = 0;
                    c = snapshot.getValue(Context.class);
                    String addd = adadad.substring(10,17);
                    dataList.add(new ItemData(cat[0], tititi,toctoc, moneymoney, addd));


                }
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // string 배열에다가 집어 넣고 그것을 돌려서 리사이클 뷰 만들자
        int SIZE = 100000;
        Context[] ctt = {};
        int len = ctt.length;

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference reference2 = database2.getReference("context").child("969833");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                Context ctx = snapshot2.getValue(Context.class);
                if(ctx != null){
                    String a = ctx.title;
                    int k = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // 메인플로팅 버튼 클릭
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();

            }
        });

        // 카메라 플로팅 버튼 클릭
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Intent intent = new Intent(getApplicationContext(), CommActivity.class);
               Intent intent = new Intent(getApplicationContext(), CommActivity.class);
               startActivity(intent);
               //Toast.makeText(MainActivity.this, "카메라 버튼 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        // 수정 플로팅 버튼 클릭
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), writeActivity.class);
                intent.putExtra("user_address", user_address);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

    }
    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    // 플로팅 액션 버튼 클릭시 애니메이션 효과
    public void toggleFab() {
        if(fabMain_status) {
            // 플로팅 액션 버튼 닫기
            // 애니메이션 추가
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCamera, "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", 0f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_baseline_add_24);

        }else {
            // 플로팅 액션 버튼 열기
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCamera, "translationY", -300f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", -600f);
            fe_animation.start();
            // 메인 플로팅 이미지 변경
            fabMain.setImageResource(R.drawable.ic_baseline_clear_24);
        }
        // 플로팅 버튼 상태 변경
        fabMain_status = !fabMain_status;
    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(), "Item : "+position, Toast.LENGTH_SHORT).show();
        Intent it = new Intent(MainActivity.this, ContentActivity.class);

        it.putExtra("user_id", user_id);


        it.putExtra("user_address", user_address);
        ItemData item = dataList.get(position);

        it.putExtra("content", contextArr[position]);
        it.putExtra("title", item.title);
        it.putExtra("money", item.money);
        // test 4개 데이터

        it.putExtra("title", titleArr[position]);
        it.putExtra("address", addArr[position]);
        it.putExtra("money", moneyArr[position]);
        it.putExtra("typeOfContext", typeOfContextArr[position]);
        it.putExtra("negotiation", negoArr[position]);
        it.putExtra("currentStat", currentStatArr[position]);


        startActivity(it);
    }

    public void onTitleClicked(int position) {
        Toast.makeText(getApplicationContext(), "Title : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onContentClicked(int position) {
        Toast.makeText(getApplicationContext(), "Content : "+position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onImageViewClicked(int position) {
        Toast.makeText(getApplicationContext(), "Image : "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoneyClicked(int position) {
        Toast.makeText(getApplicationContext(), "Image : "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddressClicked(int position) {
        Toast.makeText(getApplicationContext(), "Image : "+position, Toast.LENGTH_SHORT).show();
    }

    public void onItemLongClicked(int position) {
        adapter.remove(position);
        Toast.makeText(getApplicationContext(),
                dataList.get(position).getTitle()+" is removed",Toast.LENGTH_SHORT).show();
    }
}
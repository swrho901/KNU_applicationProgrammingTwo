package com.example.petking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyRecyclerAdapter.MyRecyclerViewClickListener, SimpleTextAdapter.SimpleTextClickListener, OnMapReadyCallback {
    // , SimpleTextAdapter.SimpleTextClickListener
    ArrayList<ItemData> dataList = new ArrayList<>();
    int[] cat = {R.drawable.doggy, R.drawable.dog3,  R.drawable.dog2, R.drawable.cat1, R.drawable.cat2};

    // 검색시 같은 이름이 있는 아이템이 담길 리스트
    ArrayList<ItemData> search_list = new ArrayList<>();
    EditText editText;

    //final Geocoder geocoder = new Geocoder(this);

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
    public int[] total_read_numArr = new int[10000];

    public String[] ArrId = new String[10000];
    public String[] ArrTitle = new String[10000];
    public String[] ArrCtx = new String[10000];

    public int contextArrIdx = 0;
    public int commArrIdx = 0;


    public String user_address, user_id;
    public Uri tempUri;
    ImageView load;
    public int totalContextNum = 0;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // editText 리스터 작성
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editText.getText().toString();
                search_list.clear();

                if(searchText.equals("")){
                    adapter.setItems(dataList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int a = 0; a < dataList.size(); a++) {
                        if (dataList.get(a).title.toLowerCase().contains(searchText.toLowerCase())) {
                            search_list.add(dataList.get(a));
                        }
                        adapter.setItems(search_list);
                    }
                }
            }
        });


        // 정보받아오고 리사이클러뷰 업데이트 해야 해서 일부러 밑에다가 놔둠
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // 리싸이클러뷰 구분선
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        // 구분선 추가
        recyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);

        // 커뮤니티 리사이클러뷰

        ArrayList<String> list = new ArrayList<>();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2) ;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리싸이클러뷰 구분선
        DividerItemDecoration dividerItemDecoration2 =
                new DividerItemDecoration(recyclerView2.getContext(),new LinearLayoutManager(this).getOrientation());
        // 구분선 추가
        recyclerView2.addItemDecoration(dividerItemDecoration2);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SimpleTextAdapter adapter2 = new SimpleTextAdapter(list) ;
        recyclerView2.setAdapter(adapter2) ;
        adapter2.setOnClickListener(this::onTextClicked);



   //  이 부분은 에뮬레이터에서는 오류가 안뜨나 휴대폰과 연결할 때는 오류 뜸

        // 스토리지 불러오는것 테스트 용 코드
        /*load = (ImageView)findViewById(R.id.testimg);
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
        }*/



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
        int[] visit = new int[10000000];
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
                    int numnumnum = snapshot.child("total_read_num").getValue(Integer.class);
                    int likenunu = snapshot.child("like_number").getValue(Integer.class);
                    int moneymoney = snapshot.child("money").getValue(Integer.class);
                    int negonego = snapshot.child("negotiation").getValue(Integer.class);
                    String main_ctxctx = snapshot.child("main_context").getValue(String.class);
                    String toctoc = snapshot.child("typeOfContext").getValue(String.class);
                    //String toctoc = snapshot.child("typeOfContext").getValue(String.class);

                    total_read_numArr[contextArrIdx] = numnumnum;
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

                    String strKey = poly.getKey();
                    String id=String.valueOf(poly.child(strKey).child("id").getValue());
                    String title=String.valueOf(poly.child("title").getValue());
                    int k = 0;
                    c = snapshot.getValue(Context.class);
                    String addd = adadad.substring(10,17);

                    Bitmap icon = BitmapFactory.decodeResource(getResources(), cat[contextArrIdx - 1]);
                    dataList.add(new ItemData(icon, tititi,toctoc, moneymoney, addd));

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

        DatabaseReference mFirebaseRef2 = database.getReference("community");
        String[] visit2 = new String[1000000];
        mFirebaseRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                for (DataSnapshot poly : snapshot.getChildren()) {

                    String ididid = snapshot.child("id").getValue(String.class);
                    String mainCtx = snapshot.child("main_context").getValue(String.class);
                    int likenunu = snapshot.child("like_number").getValue(Integer.class);
                    String tititi = snapshot.child("title").getValue(String.class);

                    int contFlag = 0;
                    for(int i=0;i<commArrIdx;i++) {
                        if(visit2[i].equals(tititi)) {
                            contFlag = 1;
                            break;
                        }
                    }
                    if(contFlag == 1)
                        continue;

                    ArrId[commArrIdx] = ididid;
                    ArrTitle[commArrIdx] = tititi;
                    ArrCtx[commArrIdx] = mainCtx;
                    // like num 은 나중에

                    visit2[commArrIdx] = tititi;
                    commArrIdx++;
                    list.add(tititi + "\n" +ididid);
                }
                recyclerView2.setAdapter(adapter2);
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
               intent.putExtra("user_id", user_id);
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
    public void onTextClicked(int position){
        Toast.makeText(getApplicationContext(), "sdsdsdsd"+position, Toast.LENGTH_LONG).show();

        Intent it = new Intent(MainActivity.this, CommShow.class);

        it.putExtra("comm_title", ArrTitle[position]);
        it.putExtra("comm_content", ArrCtx[position]);
        it.putExtra("comm_id", ArrId[position]);

        startActivity(it);

    }

    @Override
    public void onItemClicked(int position) {
        Toast.makeText(getApplicationContext(), "Item : "+position, Toast.LENGTH_SHORT).show();
        Intent it = new Intent(MainActivity.this, ContentActivity.class);

        it.putExtra("user_id", ArrId[position]);

/*        Bitmap icon = BitmapFactory.decodeResource(getResources(), cat[position]);
//        it.putExtra("image", icon);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        it.putExtra("image",byteArray);*/


        it.putExtra("user_address", user_address);
        ItemData item = dataList.get(position);

        it.putExtra("content", contextArr[position]);
        it.putExtra("title", item.title);
        it.putExtra("money", item.money);
        // test 4개 데이터

        it.putExtra("total_read_num", total_read_numArr[position]);
        it.putExtra("title", titleArr[position]);
        it.putExtra("address", addArr[position]);
        it.putExtra("money", moneyArr[position]);
        it.putExtra("typeOfContext", typeOfContextArr[position]);
        it.putExtra("negotiation", negoArr[position]);
        it.putExtra("currentStat", currentStatArr[position]);


        //추가 11-30
        it.putExtra("my_id", user_id);


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

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        String str = "대구광역시 북구 복현동 573-10";
        List list = null;
        double lat = 35.8914645, lon = 128.6146076;
/*
        try {
            list = geocoder.getFromLocationName(str,10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            String city = "";
            String country = "";
            if (list.size() == 0) {

            }
            else {
                Address address = (Address)list.get(0);
                lat = address.getLatitude();
                lon = address.getLongitude();
            }
        }*/
        String address = getCurrentAddress(35.8909202, 128.6139879);



        mMap = googleMap;

        /*LatLng CHUMSEONG = new LatLng(lat, lon);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(CHUMSEONG);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);*/

        String visit3[] = new String[10000000];
        DatabaseReference mFirebaseRef3 = database.getReference("users");
        mFirebaseRef3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                int idx = 0;
                for (DataSnapshot poly : snapshot.getChildren()) {

                    final Geocoder geocoder2 = new Geocoder(MainActivity.this, Locale.getDefault());

                    String ididid = snapshot.child("id").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String sex = snapshot.child("sex").getValue(String.class);
                    int age = Integer.parseInt(snapshot.child("age").getValue(String.class));
                    int contFlag = 0;
                    for(int i=0;i<idx;i++){
                        if(visit3[i].equals(email)){
                            contFlag = 1;
                            break;
                        }
                    }
                    if(contFlag == 1)
                        continue;
                    visit3[idx] = email;
                    idx++;

                    double lat2 = 35.8914645, lon2 = 128.6146076;

                    List lst1 = null;

                    try {
                        lst1 = geocoder2.getFromLocationName(address,10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (lst1 != null) {
                        String city = "";
                        String country = "";
                        if (lst1.size() == 0) {

                        }
                        else {
                            Address address2 = (Address)lst1.get(0);
                            lat2 = address2.getLatitude();
                            lon2 = address2.getLongitude();
                        }
                    }
                    LatLng LL = new LatLng(lat2, lon2);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(LL);
                    markerOptions.title(ididid);
                    markerOptions.snippet("sex : " + sex + ", age : " + age);

                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.policedog);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 120, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    mMap.addMarker(markerOptions);

                }

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

/*
        double x1 = 35.88866614, x2 = 128.6121060;
        LatLng IllChungDam = new LatLng(x1, x2);
        markerOptions.position(IllChungDam);
        markerOptions.title("일청담");
        markerOptions.snippet("한국의 수도");
        mMap.addMarker(markerOptions);
*/


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent it = new Intent(MainActivity.this, ContentActivity.class);

                String s = arg0.getTitle();

                int flag= 0;

                for(int i=commArrIdx - 1;i >= 0;i--) {
                    if (ArrId[i].equals(s)) {
                        //일단 test
                        it.putExtra("user_id", ArrId[i]);

                        it.putExtra("user_address", user_address);
                        ItemData item = dataList.get(i);

                       // it.putExtra("image", BitmapFactory.decodeResource(getResources(), cat[i]));
/*
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), cat[i]);
//        it.putExtra("image", icon);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        it.putExtra("image",byteArray);*/

                        it.putExtra("content", contextArr[i]);
                        it.putExtra("title", item.title);
                        it.putExtra("money", item.money);
                        // test 4개 데이터

                        it.putExtra("total_read_num", total_read_numArr[i]);
                        it.putExtra("title", titleArr[i]);
                        it.putExtra("address", addArr[i]);
                        it.putExtra("money", moneyArr[i]);
                        it.putExtra("typeOfContext", typeOfContextArr[i]);
                        it.putExtra("negotiation", negoArr[i]);
                        it.putExtra("currentStat", currentStatArr[i]);

                        //추가 11-30
                        it.putExtra("my_id", user_id);


                        startActivity(it);
                        flag = 1;
                        break;
                    }
                    if (flag == 0) {
                        Toast.makeText(getApplicationContext(), "게시글 없음", Toast.LENGTH_LONG).show();

                    }
                }

                Log.d("mGoogleMap1", "Activity_Calling");
            }
        });

/*        // 마커 클릭에 대한 이벤트 처리.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String M = marker.getSnippet();
                Intent it = new Intent(getApplicationContext(), ContentActivity.class);
                startActivity(it);
                return false;
            }
        });*/

        LatLng CHUMSEONG = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CHUMSEONG, 15));

    }

    // 이 함수는 위도경도를 주소로 바꾸는 함수 //
    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }

}
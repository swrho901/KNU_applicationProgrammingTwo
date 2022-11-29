package com.example.petking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Chat> mDataset;
    String stMyEmail = "";

    public static class ViewHolder extends RecyclerView.ViewHolder { // 아이템 뷰를 저장하는 뷰홀더
        private final TextView tvChat;

        public ViewHolder(View view) {
            super(view);
            tvChat = (TextView) view.findViewById(R.id.tvChat); //  채팅문자가 입력될 텍스트뷰 찾기
        }

        public TextView getTextView() { // 채팅문자가 입력된 텍스트뷰 가져오기
            return tvChat;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataset.get(position).email.equals(stMyEmail)) { // email과 stMyEmail과 같으면
            return 1; // 내 메시지
        } else { // stMyEail과 다르면
            return 2;  // 내 메시지가 아님
        }
    }

    // Chat형 어레이 리스트 myDataset을 처음에 전역변수로 선언한  mDataset에 대입
    public MyAdapter(ArrayList<Chat> myDataset, String stEmail) {
        mDataset = myDataset;
        this.stMyEmail = stEmail;
    }

    // 뷰홀더를 생성(레이아웃 생성)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { // 아이템들을 어떻게 보여줄 것인가
        View view = LayoutInflater.from(viewGroup.getContext()) // 상대방 메시지이면
                .inflate(R.layout.left_text_view, viewGroup, false); // 말풍선이 왼쪽에서 나타나는 xml 파일 인플레이션

        if(viewType == 1) { // 내 메시지이면
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.right_text_view, viewGroup, false); // 말풍선이 오른쪽에서 나타나는 xml 파일 인플레이션
        }
        return new ViewHolder(view);
    }

    // 뷰홀더가 재활용될 때 실행
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(mDataset.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

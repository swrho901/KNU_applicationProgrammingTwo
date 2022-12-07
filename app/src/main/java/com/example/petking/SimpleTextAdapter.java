package com.example.petking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    public SimpleTextAdapter(ArrayList<String> mData) {
        this.mData = mData ;
    }


    public interface SimpleTextClickListener{
        void onTextClicked(int position);
    }

    public SimpleTextClickListener mListener;

    public void setOnClickListener(SimpleTextAdapter.SimpleTextClickListener listener) {
        this.mListener = listener;
    }
    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1 ;

        public SimpleTextClickListener mListener2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.commTitle) ;
            //itemView.setOnClickListener();
        }

    }



    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        android.content.Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) ((android.content.Context) context).getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.comm_list, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull final SimpleTextAdapter.ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.textView1.setText(text);
        if (mListener != null) {
            final int pos = position;
            holder.textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTextClicked(pos);
                }
            });
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

}
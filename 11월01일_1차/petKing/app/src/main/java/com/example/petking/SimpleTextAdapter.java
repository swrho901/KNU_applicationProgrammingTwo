package com.example.petking;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    private ArrayList<Community> community = null ;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    public SimpleTextAdapter(ArrayList<Community> community) {
        this.community = community ;
    }


    public interface SimpleTextClickListener{
        void onItemClicked2(int position);
        void onTitleClicked2(int position);
        void onContentClicked2(int position);
        void onItemLongClicked2(int position);
        void onImageViewClicked2(int position);
    }

    public SimpleTextClickListener mListener;

    public void setOnClickListener(SimpleTextAdapter.SimpleTextClickListener listener) {
        this.mListener = listener;
    }
    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        ImageView image;


        public SimpleTextClickListener mListener2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView) ;
            title = itemView.findViewById(R.id.commTitle) ;
            content = itemView.findViewById(R.id.commContent) ;
            image = itemView.findViewById(R.id.image);

            //이미지뷰 원형으로 표시
            image.setBackground(new ShapeDrawable(new OvalShape()));
            image.setClipToOutline(true);
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
        Community comm = community.get(position);
        holder.title.setText(comm.getComTitle());
        holder.content.setText(comm.getComContent());
        holder.image.setImageResource(comm.getComImage());
        if (mListener != null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked2(pos);
                }
            });
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTitleClicked2(pos);
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onContentClicked2(pos);
                }
            });
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onImageViewClicked2(pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemLongClicked2(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return community.size();
    }

    //리스트 삭제 이벤트
    public void remove(int position){
        try {
            community.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}
package com.example.petking;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private ArrayList<ItemData> itemData;
    public MyRecyclerAdapter(ArrayList<ItemData> itemData) {
        this.itemData = itemData;
    }

    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onTitleClicked(int position);
        void onContentClicked(int position);
        void onItemLongClicked(int position);
        void onImageViewClicked(int position);
        void onMoneyClicked(int position);
        void onAddressClicked(int position);
    }


    private MyRecyclerViewClickListener mListener;

    public void setOnClickListener(MyRecyclerViewClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.postlist_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ItemData item = itemData.get(position);

        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.image.setImageResource(item.getImage());
        holder.money.setText(Integer.toString(item.getMoney()) + " KRW");
        holder.address.setText(item.getAddress());

        //화면에 데이터 담기
        holder.setItem(item);

        //아이템 클릭 이벤트
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int mPostion = holder.getBindingAdapterPosition();

                Intent intent = new Intent(view.getContext(), ContentActivity.class);

                intent.putExtra("title"    ,itemData.get(mPostion).getTitle()); //제목
                intent.putExtra("content"  ,itemData.get(mPostion).getContent()); //내용
                intent.putExtra("address"   ,itemData.get(mPostion).getAddress()); //주소
                intent.putExtra("money"   ,itemData.get(mPostion).getMoney()); //가격

                view.getContext().startActivity(intent);
            }
        });

        if (mListener != null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(pos);
                }
            });
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTitleClicked(pos);
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onContentClicked(pos);
                }
            });
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onImageViewClicked(pos);
                }
            });
            holder.money.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoneyClicked(pos);
                }
            });
            holder.address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddressClicked(pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemLongClicked(holder.getAdapterPosition());
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView image;
        TextView money;
        TextView address;
        CardView card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.mojip);
            image = itemView.findViewById(R.id.image);
            money = itemView.findViewById((R.id.money));
            address = itemView.findViewById(R.id.address);
            card_view = itemView.findViewById(R.id.layout_container);

            //이미지뷰 원형으로 표시
            image.setBackground(new ShapeDrawable(new OvalShape()));
            image.setClipToOutline(true);

        }

        public void setItem(ItemData itemdata){

            title.setText(itemdata.getTitle());
            content.setText(itemdata.getContent());
            money.setText(itemdata.getMoney());
            address.setText(itemdata.getAddress());
        }
    }

    //리스트 삭제 이벤트
    public void remove(int position){
        try {
            itemData.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}
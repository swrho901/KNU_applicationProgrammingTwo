package com.example.petking;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ItemViewHolder> {

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

    // 뷰홀더 상속 및 구현
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected TextView content;
        protected ImageView image;
        protected TextView money;
        protected TextView address;
        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.mojip);
            this.image = itemView.findViewById(R.id.imageProfile);
            this.money = itemView.findViewById((R.id.money));
            this.address = itemView.findViewById(R.id.address);

            //이미지뷰 원형으로 표시
            image.setBackground(new ShapeDrawable(new OvalShape()));
            image.setClipToOutline(true);
        }

        public void onBind(ItemData it){
            title.setText(it.getTitle());
            content.setText(it.getContent());
            money.setText(Integer.toString(it.getMoney()) + " KRW");
            address.setText(it.getAddress());

            String str1 = Integer.toString(it.getImage());

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://petking-51b02.appspot.com/");
            StorageReference storageReference = storage.getReference();
            storageReference.child(str1).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(itemView).load(uri).into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewholder, final int position) {
        viewholder.onBind(itemData.get(position));

        final int pos = position;

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(pos);
            }
        });
        viewholder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTitleClicked(pos);
            }
        });

        viewholder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(pos);
            }
        });

        viewholder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImageViewClicked(pos);
            }
        });
        viewholder.money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoneyClicked(pos);
            }
        });
        viewholder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddressClicked(pos);
            }
        });
        viewholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onItemLongClicked(viewholder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemData.size();
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


    public void setItems(ArrayList<ItemData> list){
        itemData = list;
        notifyDataSetChanged();
    }
}
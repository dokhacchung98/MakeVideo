package com.khacchung.makevideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khacchung.makevideo.BR;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.activity.EditImageActivity;
import com.khacchung.makevideo.databinding.RowImageHorizontalBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListImageHorizontalAdapter extends RecyclerView.Adapter<ListImageHorizontalAdapter.MyViewHolder> {

    private ArrayList<MyImageModel> listItemImage;
    private Context context;
    private RowImageHorizontalBinding itemImageBinding;
    private MySelectedItemListener listener;

    public ListImageHorizontalAdapter(Context context, ArrayList<MyImageModel> listItemImage, MySelectedItemListener listener) {
        this.listItemImage = listItemImage;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemImageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_image_horizontal,
                parent,
                false
        );
        return new MyViewHolder(itemImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyImageModel item = listItemImage.get(position);
        holder.bind(item);
        holder.itemImageBinding.setHandler(new MyClickHandler() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onClickWithData(View view, Object value) {
                if (view.getId() == R.id.btnEdit) {
                    listener.selectedItem(value, CodeSelectedItem.CODE_SELECT, position);
                } else if (view.getId() == R.id.imgThumbnail) {
                    MyImageModel model = (MyImageModel) value;
                    if (model.getPathImage() == null || model.getPathImage().isEmpty()) {
                        listener.selectedItem(value, CodeSelectedItem.CODE_ADD, position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemImage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowImageHorizontalBinding itemImageBinding;

        MyViewHolder(RowImageHorizontalBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            MyImageModel model = (MyImageModel) obj;

            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
            if (model.getPathImage() == null || model.getPathImage().isEmpty()) {
                itemImageBinding.imgThumbnail.setImageResource(R.drawable.add_img);
                itemImageBinding.btnEdit.setVisibility(View.GONE);
            } else {
                itemImageBinding.btnEdit.setVisibility(View.VISIBLE);
            }
        }
    }
}
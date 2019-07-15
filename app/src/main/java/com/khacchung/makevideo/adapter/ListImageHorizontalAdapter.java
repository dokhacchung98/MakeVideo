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
import com.khacchung.makevideo.model.MyImageModel;

import java.util.ArrayList;

public class ListImageHorizontalAdapter extends RecyclerView.Adapter<ListImageHorizontalAdapter.MyViewHolder>
        implements MyClickHandler {

    private ArrayList<MyImageModel> listItemImage;
    private Context context;
    private RowImageHorizontalBinding itemImageBinding;

    public ListImageHorizontalAdapter(Context context, ArrayList<MyImageModel> listItemImage) {
        this.listItemImage = listItemImage;
        this.context = context;
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
        holder.itemImageBinding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listItemImage.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onClickWithData(View view, Object value) {
        MyImageModel model = (MyImageModel) value;
        if (view.getId() == R.id.btnEdit) {
            EditImageActivity.startInternt(context, model.getPathImage(), itemImageBinding.imgThumbnail, true);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowImageHorizontalBinding itemImageBinding;

        MyViewHolder(RowImageHorizontalBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
        }
    }
}
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
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.databinding.RowFrameBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyFrameModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListFramesAdapter extends RecyclerView.Adapter<ListFramesAdapter.MyViewHolder>
        implements MyClickHandler {

    private ArrayList<MyFrameModel> listFrames;
    private Context context;
    private RowFrameBinding binding;
    private MySelectedItemListener listener;
    private String currentFrame;

    public ListFramesAdapter(Context context, ArrayList<MyFrameModel> listFrames, MySelectedItemListener listener) {
        this.listFrames = listFrames;
        this.context = context;
        this.listener = listener;
        getCurrentTheme();
    }

    public void getCurrentTheme() {
        MyApplication application = MyApplication.getInstance();
        currentFrame = application.getFrameVideo();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_frame,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyFrameModel item = listFrames.get(position);
        holder.bind(item);
        holder.itemImageBinding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listFrames.size();
    }

    @Override
    public void onClick(View view) {;
    }

    @Override
    public void onClickWithData(View view, Object value) {
        MyFrameModel effect = (MyFrameModel) value;
        listener.selectedItem(effect, CodeSelectedItem.CODE_SELECT);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowFrameBinding itemImageBinding;

        MyViewHolder(RowFrameBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
            MyFrameModel tmp = (MyFrameModel) obj;
            if (tmp.getPathFrame().isEmpty()) {
                itemImageBinding.imgThumbnail.setImageResource(0);
            }
            if (tmp.getPathFrame().equals(currentFrame)) {
                itemImageBinding.imgChecked.setVisibility(View.VISIBLE);
            } else {
                itemImageBinding.imgChecked.setVisibility(View.GONE);
            }
        }
    }
}
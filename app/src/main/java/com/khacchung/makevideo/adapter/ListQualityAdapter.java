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
import com.khacchung.makevideo.databinding.RowQualityBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyQualityModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListQualityAdapter extends RecyclerView.Adapter<ListQualityAdapter.MyViewHolder>
        implements MyClickHandler {

    private ArrayList<MyQualityModel> listQuality;
    private Context context;
    private RowQualityBinding binding;
    private MySelectedItemListener listener;

    public ListQualityAdapter(Context context, ArrayList<MyQualityModel> list, MySelectedItemListener listener) {
        this.listQuality = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_quality,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyQualityModel item = listQuality.get(position);
        holder.bind(item);
        holder.itemImageBinding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listQuality.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onClickWithData(View view, Object value) {
        MyQualityModel quality = (MyQualityModel) value;
        listener.selectedItem(quality, CodeSelectedItem.CODE_SELECT);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowQualityBinding itemImageBinding;

        MyViewHolder(RowQualityBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
        }
    }
}
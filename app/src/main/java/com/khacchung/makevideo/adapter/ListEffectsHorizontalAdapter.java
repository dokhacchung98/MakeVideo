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
import com.khacchung.makevideo.databinding.RowEffectBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListEffectsHorizontalAdapter extends RecyclerView.Adapter<ListEffectsHorizontalAdapter.MyViewHolder>
        implements MyClickHandler {

    private ArrayList<THEMES> listEffect;
    private Context context;
    private RowEffectBinding binding;
    private MySelectedItemListener listener;
    private THEMES currentTheme;

    public ListEffectsHorizontalAdapter(Context context, ArrayList<THEMES> listEffect, MySelectedItemListener listener) {
        this.listEffect = listEffect;
        this.context = context;
        this.listener = listener;
        getCurrentTheme();
    }

    public void getCurrentTheme() {
        MyApplication application = MyApplication.getInstance();
        currentTheme = application.getSeletedTheme();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_effect,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        THEMES item = listEffect.get(position);
        holder.bind(item);
        holder.itemImageBinding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listEffect.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onClickWithData(View view, Object value) {
        THEMES effect = (THEMES) value;
        listener.selectedItem(effect, CodeSelectedItem.CODE_SELECT);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowEffectBinding itemImageBinding;

        MyViewHolder(RowEffectBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
            if (currentTheme != null) {
                THEMES tmp = (THEMES) obj;
                if (tmp.getName().equals(currentTheme.getName())) {
                    itemImageBinding.imgChecked.setVisibility(View.VISIBLE);
                } else {
                    itemImageBinding.imgChecked.setVisibility(View.GONE);
                }
            }
        }
    }
}
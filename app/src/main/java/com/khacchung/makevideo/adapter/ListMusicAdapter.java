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
import com.khacchung.makevideo.databinding.RowMusicBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListMusicAdapter extends RecyclerView.Adapter<ListMusicAdapter.MyViewHolder>
        implements MyClickHandler {

    private ArrayList<MyMusicModel> listMusic;
    private Context context;
    private RowMusicBinding binding;
    private MySelectedItemListener listener;

    public ListMusicAdapter(Context context, ArrayList<MyMusicModel> listMusic, MySelectedItemListener listener) {
        this.listMusic = listMusic;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_music,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyMusicModel item = listMusic.get(position);
        holder.bind(item);
        holder.itemImageBinding.setHandler(this);
    }

    @Override
    public int getItemCount() {
        return listMusic.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onClickWithData(View view, Object value) {
        MyMusicModel music = (MyMusicModel) value;
        switch (view.getId()) {
            case R.id.ln_select: {
                listener.selectedItem(music, CodeSelectedItem.CODE_SELECT);
            }
            break;
            case R.id.img_cut:
                listener.selectedItem(music, CodeSelectedItem.CODE_CUT);
                break;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowMusicBinding itemImageBinding;

        MyViewHolder(RowMusicBinding itemImageBinding) {
            super(itemImageBinding.getRoot());
            this.itemImageBinding = itemImageBinding;
        }

        public void bind(Object obj) {
            itemImageBinding.setVariable(BR.model, obj);
            itemImageBinding.executePendingBindings();
        }
    }
}
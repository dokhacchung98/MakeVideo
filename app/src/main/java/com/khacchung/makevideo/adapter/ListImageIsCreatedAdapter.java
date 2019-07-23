package com.khacchung.makevideo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khacchung.makevideo.BR;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.RowImageCreatedBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListImageIsCreatedAdapter extends RecyclerView.Adapter<ListImageIsCreatedAdapter.MyViewHolder> {
    private ArrayList<String> listImages;
    private RowImageCreatedBinding binding;
    private BaseActivity baseActivity;
    private MySelectedItemListener listener;

    public ListImageIsCreatedAdapter(BaseActivity baseActivity, ArrayList<String> listImages, MySelectedItemListener listener) {
        this.listImages = listImages;
        this.baseActivity = baseActivity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(baseActivity),
                R.layout.row_image_created,
                parent,
                false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String model = listImages.get(position);
        holder.bind(model);
        holder.binding.setHandler(new MyClickHandler() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onClickWithData(View view, Object value) {
                switch (view.getId()) {
                    case R.id.imgThumbnail:
                        listener.selectedItem(value, CodeSelectedItem.CODE_SHOW_IMAGE, position);
                        break;
                    case R.id.btnDelete:
                        listener.selectedItem(value, CodeSelectedItem.CODE_REMOVE, position);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowImageCreatedBinding binding;

        MyViewHolder(RowImageCreatedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object o) {
            binding.setVariable(BR.model, o);
            binding.executePendingBindings();
        }
    }
}

package com.khacchung.makevideo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.LayoutColorPickerItemListBinding;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Integer> colorPickerColors;
    private OnColorPickerClickListener onColorPickerClickListener;

    ColorPickerAdapter(@NonNull Context context, @NonNull List<Integer> colorPickerColors) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = colorPickerColors;
    }

    public ColorPickerAdapter(@NonNull Context context) {
        this(context, getDefaultColors(context));
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutColorPickerItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.layout_color_picker_item_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.colorPickerView.setBackgroundColor(colorPickerColors.get(position));
    }

    @Override
    public int getItemCount() {
        return colorPickerColors.size();
    }

    private void buildColorPickerView(View view, int colorCode) {
        view.setVisibility(View.VISIBLE);

        ShapeDrawable biggerCircle = new ShapeDrawable(new OvalShape());
        biggerCircle.setIntrinsicHeight(20);
        biggerCircle.setIntrinsicWidth(20);
        biggerCircle.setBounds(new Rect(0, 0, 20, 20));
        biggerCircle.getPaint().setColor(colorCode);

        ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
        smallerCircle.setIntrinsicHeight(5);
        smallerCircle.setIntrinsicWidth(5);
        smallerCircle.setBounds(new Rect(0, 0, 5, 5));
        smallerCircle.getPaint().setColor(Color.WHITE);
        smallerCircle.setPadding(10, 10, 10, 10);
        Drawable[] drawables = {smallerCircle, biggerCircle};

        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        view.setBackgroundDrawable(layerDrawable);
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View colorPickerView;

        public ViewHolder(LayoutColorPickerItemListBinding binding) {
            super(binding.getRoot());
            colorPickerView = binding.colorPickerView;
            itemView.setOnClickListener(v -> {
                if (onColorPickerClickListener != null)
                    onColorPickerClickListener.onColorPickerClickListener(colorPickerColors.get(getAdapterPosition()));
            });
        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }

    /**
     * Create list color picker
     */
    public static List<Integer> getDefaultColors(Context context) {
        ArrayList<Integer> colorPickerColors = new ArrayList<>();
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_1));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_2));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_3));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_4));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_5));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_6));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_7));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_8));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_9));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_10));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_11));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_12));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_13));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_14));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_15));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_16));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_17));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_18));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_19));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_20));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_21));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_22));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_23));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_24));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_25));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_26));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_27));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_28));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_29));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_30));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_31));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_32));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_33));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_34));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_35));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_36));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_37));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_38));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_39));
        colorPickerColors.add(ContextCompat.getColor(context, R.color.color_material_40));
        return colorPickerColors;
    }
}

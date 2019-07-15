package com.khacchung.makevideo.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.FragmentBottomStickerEmojiDialogBinding;
import com.khacchung.makevideo.databinding.RowStickerBinding;

public class StickerBSFragment extends BottomSheetDialogFragment {
    private FragmentBottomStickerEmojiDialogBinding binding;

    public StickerBSFragment() {
    }

    private StickerListener mStickerListener;

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_bottom_sticker_emoji_dialog, null, false);
        dialog.setContentView(binding.getRoot());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) binding.getRoot().getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) binding.getRoot().getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        RecyclerView rvEmoji = binding.rvEmoji;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Create list sticker
     */
    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

        int[] stickerList = new int[]{R.drawable.a1
                , R.drawable.a2
                , R.drawable.a3
                , R.drawable.a4
                , R.drawable.a5
                , R.drawable.a6
                , R.drawable.a7
                , R.drawable.a8
                , R.drawable.a9
                , R.drawable.a13
                , R.drawable.a15
                , R.drawable.a17
                , R.drawable.a21
                , R.drawable.a23
                , R.drawable.a27
                , R.drawable.a29
                , R.drawable.a30
                , R.drawable.a32
                , R.drawable.a34
                , R.drawable.a37
                , R.drawable.a38
                , R.drawable.a39
                , R.drawable.a40
                , R.drawable.a42
                , R.drawable.a44
                , R.drawable.a45
                , R.drawable.b1
                , R.drawable.b3
                , R.drawable.b4
                , R.drawable.b6
                , R.drawable.b7
                , R.drawable.b8
                , R.drawable.b9
                , R.drawable.b20
                , R.drawable.b21
                , R.drawable.b22
                , R.drawable.b23
                , R.drawable.b24
                , R.drawable.l1
                , R.drawable.l2
                , R.drawable.l3
                , R.drawable.l4
                , R.drawable.l5
                , R.drawable.l6
                , R.drawable.l7
                , R.drawable.l8
                , R.drawable.l9
                , R.drawable.s2
                , R.drawable.s9
                , R.drawable.s10
                , R.drawable.s11
                , R.drawable.s12
                , R.drawable.s13
                , R.drawable.s14
                , R.drawable.s15
                , R.drawable.s17
                , R.drawable.s18
                , R.drawable.s19
                , R.drawable.s20
                , R.drawable.s22
                , R.drawable.s24
                , R.drawable.s25
                , R.drawable.s26
                , R.drawable.s27
                , R.drawable.s28
                , R.drawable.s30
                , R.drawable.s31
                , R.drawable.s32
                , R.drawable.s33
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RowStickerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_sticker, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.imgSticker.setImageResource(stickerList[position]);
        }

        @Override
        public int getItemCount() {
            return stickerList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(RowStickerBinding binding) {
                super(binding.getRoot());
                imgSticker = binding.imgSticker;

                itemView.setOnClickListener(v -> {
                    if (mStickerListener != null) {
                        mStickerListener.onStickerClick(
                                BitmapFactory.decodeResource(getResources(),
                                        stickerList[getLayoutPosition()]));
                    }
                    dismiss();
                });
            }
        }
    }

    private String convertEmoji(String emoji) {
        String returnedEmoji;
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
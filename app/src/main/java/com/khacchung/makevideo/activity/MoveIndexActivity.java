package com.khacchung.makevideo.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.ImageMoveAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityMoveIndexBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;
import java.util.Collections;

public class MoveIndexActivity extends BaseActivity implements MySelectedItemListener {
    private ActivityMoveIndexBinding binding;
    private MyApplication myApplication;
    private ArrayList<MyImageModel> listImage;
    private ImageMoveAdapter adapter;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, MoveIndexActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar("Chỉnh Thứ Tự Ảnh");
        enableBackButton();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_move_index);
        myApplication = MyApplication.getInstance();
        listImage = myApplication.getListIamge();
        if (listImage == null || listImage.size() < 3) {
            ShowLog.ShowLog(this, binding.getRoot(), "Có lỗi, vui lòng thử lại", false);
            finish();
        }

        initView();
    }

    private void initView() {
        adapter = new ImageMoveAdapter(this, listImage, this);

        binding.setLayoutManager(new GridLayoutManager(this, 2));
        binding.setMyAdapter(adapter);

        adapter.notifyDataSetChanged();

        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(binding.rvListImage);
    }

    ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Collections.swap(listImage, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
        }
    };

    @Override
    public void selectedItem(Object obj, int code) {

    }

    @Override
    public void selectedItem(Object obj, int code, int p) {
        if (code == CodeSelectedItem.CODE_REMOVE && p < listImage.size() && listImage.size() > 3) {
            listImage.remove(p);
            adapter.notifyDataSetChanged();
        } else if (listImage.size() <= 3) {
            ShowLog.ShowLog(this, binding.getRoot(), "Bạn phải có ít nhất 3 ảnh trong danh sách", false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next_and_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                if (listImage.size() > 2) {
                    CreateVideoActivity.startIntent(this);
                    finish();
                } else {
                    ShowLog.ShowLog(this, binding.getRoot(), "Bạn phải có ít nhất 3 ảnh trong danh sách", false);
                }
                break;
            case R.id.action_add:
                SelectImageActivity.startIntent(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SelectImageActivity.startIntent(this);
    }
}

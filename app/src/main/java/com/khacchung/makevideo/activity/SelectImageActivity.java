package com.khacchung.makevideo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.FolderAdapter;
import com.khacchung.makevideo.adapter.ImageSeletedAdapter;
import com.khacchung.makevideo.adapter.ListImageAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivitySelectImageBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyFolderModel;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.io.File;
import java.util.ArrayList;

public class SelectImageActivity extends BaseActivity implements MySelectedItemListener, MyClickHandler {
    protected ActivitySelectImageBinding binding;
    private ArrayList<MyFolderModel> listFolder;
    private ArrayList<MyImageModel> listImage;
    private ArrayList<MyImageModel> listImageSelected;
    private ArrayList<MyImageModel> listImageByFolder;

    private FolderAdapter folderAdapter;
    private ListImageAdapter listImageAdapter;
    private ImageSeletedAdapter imageSeletedAdapter;

    private MyApplication myApplication;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        setTitleToolbar("Danh sách ảnh");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_image);

        myApplication = MyApplication.getInstance();


        listFolder = new ArrayList<>();
        listImage = new ArrayList<>();
        listImageSelected = new ArrayList<>();
        listImageByFolder = new ArrayList<>();

        folderAdapter = new FolderAdapter(this, listFolder, this);
        listImageAdapter = new ListImageAdapter(this, listImageByFolder, true);
        listImageAdapter.setSelectedItemListener(this);
        imageSeletedAdapter = new ImageSeletedAdapter(this, listImageSelected, this);

        binding.setFolderAdapter(folderAdapter);
        binding.setListImageAdapter(listImageAdapter);
        binding.setSeletedAdapter(imageSeletedAdapter);

        binding.setLayoutManagerFolder(new GridLayoutManager(this, 1));
        binding.setLayoutManagerImage(new GridLayoutManager(this, 2));
        binding.setLayoutManagerSelected(new GridLayoutManager(this, 4));

        binding.setHandler(this);


        if (myApplication.getListIamge().size() > 2) {
            listImageSelected = myApplication.getListIamge();
            imageSeletedAdapter.notifyDataSetChanged();
            bindingSizeSeleted();
        }
        getAllFolderContaningImage();
    }

    private void getAllFolderContaningImage() {
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_data", "_id", "bucket_display_name", "bucket_id", "datetaken"},
                null, null, "bucket_display_name DESC");
        assert query != null;
        if (query.moveToFirst()) {
            int columnIndex = query.getColumnIndex("bucket_display_name");
            int columnIndex3 = query.getColumnIndex("datetaken");
            do {
                String imagePath = query.getString(query.getColumnIndex("_data"));
                if (!imagePath.endsWith(".gif")) {
                    query.getString(columnIndex3);
                    String string2 = query.getString(columnIndex);
                    boolean check = true;
                    String path = new File(imagePath).getParent();
                    for (MyFolderModel t : listFolder) {
                        if (t.getPathFolder().trim().equals(path.trim())) {
                            check = false;
                            break;
                        }
                    }

                    listImage.add(new MyImageModel(imagePath, false, path));

                    if (check) {
                        listFolder.add(new MyFolderModel(string2, path, false));
                    }
                }
            } while (query.moveToNext());
        }

        if (listFolder.size() > 0) {
            listFolder.get(0).setSelected(true);
            getAllImageFromPathFolder(listFolder.get(0).getPathFolder());
        }

    }

    private void getAllImageFromPathFolder(String pathFolder) {
        listImageByFolder.clear();
        for (MyImageModel tmp : listImage) {
            if (tmp.getPathParent().trim().equals(pathFolder.trim())) {
                listImageByFolder.add(tmp);
                bindingNumberSeletedOneImage(tmp);
            }
        }
        showItemSeleted();
    }

    @Override
    public void selectedItem(Object obj, int code) {
        switch (code) {
            case CodeSelectedItem.CODE_SELECT_FOLDER:
                MyFolderModel folderModel = (MyFolderModel) obj;
                folderModel.setSelected(false);
                for (MyFolderModel t : listFolder) {
                    t.setSelected(false);
                }
                int indexFolder = listFolder.indexOf(folderModel);
                if (indexFolder != -1) {
                    folderModel.setSelected(true);
                    listFolder.set(indexFolder, folderModel);
                    getAllImageFromPathFolder(listFolder.get(indexFolder).getPathFolder());
                }
                break;
            case CodeSelectedItem.CODE_SELECT:
                MyImageModel tmp = (MyImageModel) obj;
                listImageSelected.add(tmp);
                bindingNumberSeletedOneImage(tmp);
                imageSeletedAdapter.notifyDataSetChanged();
                bindingSizeSeleted();
                showItemSeleted();
                break;
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {
        if (CodeSelectedItem.CODE_REMOVE == code) {
            MyImageModel tmp1 = (MyImageModel) obj;
            listImageSelected.remove(p);
            if (listImageByFolder.contains(obj)) {
                bindingNumberSeletedOneImage(tmp1);
            }
            folderAdapter.notifyDataSetChanged();
            imageSeletedAdapter.notifyDataSetChanged();
            bindingSizeSeleted();
            showItemSeleted();
        }
    }

    private void showItemSeleted() {
        for (MyImageModel tmp : listImageSelected) {
            if (listImageByFolder.indexOf(tmp) != -1) {
                listImageByFolder.get(listImageByFolder.indexOf(tmp)).setSelected(true);
            }
        }
        listImageAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void bindingSizeSeleted() {
        binding.txtSizeSelected.setText(listImageSelected.size() + "");
    }

    private void bindingNumberSeletedOneImage(MyImageModel tmp) {
        int count = 0;
        for (MyImageModel model : listImageSelected) {
            if (model.equals(tmp)) {
                count++;
            }
        }
        if (count > 0 && listImageByFolder.contains(tmp))
            listImageByFolder.get(listImageByFolder.indexOf(tmp)).setSelected(true);
        else if (listImageByFolder.contains(tmp)) {
            listImageByFolder.get(listImageByFolder.indexOf(tmp)).setSelected(false);
        }
        listImageByFolder.get(listImageByFolder.indexOf(tmp)).setNumberOfSeleted(count);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (listImageSelected.size() > 2) {
                myApplication.setListIamge(listImageSelected);
                MoveIndexActivity.startIntent(this);
                finish();
            } else {
                ShowLog.ShowLog(this, binding.getRoot(), "Bạn phải có ít nhất 3 ảnh trong danh sách", false);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imageSeletedAdapter.notifyDataSetChanged();
        bindingSizeSeleted();

        for (MyImageModel model : listImageByFolder) {
            bindingNumberSeletedOneImage(model);
        }

        listImageAdapter.notifyDataSetChanged();
    }
}

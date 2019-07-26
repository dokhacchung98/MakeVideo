package com.khacchung.makevideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.EditingToolsAdapter;
import com.khacchung.makevideo.adapter.FilterViewAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityEditImageBinding;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.fragment.EmojiBSFragment;
import com.khacchung.makevideo.fragment.PropertiesBSFragment;
import com.khacchung.makevideo.fragment.StickerBSFragment;
import com.khacchung.makevideo.fragment.TextEditorDialogFragment;
import com.khacchung.makevideo.handler.FilterListener;
import com.khacchung.makevideo.util.ToolType;

import java.io.File;
import java.io.IOException;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditImageActivity extends BaseActivity implements EditingToolsAdapter.OnItemSelected,
        FilterListener,
        View.OnClickListener,
        StickerBSFragment.StickerListener,
        OnPhotoEditorListener,
        EmojiBSFragment.EmojiListener,
        PropertiesBSFragment.Properties {
    public static final String URI_IMAGE = "URI_IMAGE";
    public static final String EXTRA_REPLACE = "EXTRA_REPLACE";
    public static final String PATH_IMAGE_NEW = "PATH_IMAGE_NEW";
    public static final String INDEX_IMAGE = "INDEX_IMAGE";
    public static final int REQUEST_CODE_EDIT_IMAGE = 456;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private static final String TAG = EditImageActivity.class.getName();
    private ActivityEditImageBinding binding;
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;
    private RecyclerView mRvTools, mRvFilters;
    private EditingToolsAdapter mEditingToolsAdapter;
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private String uri;
    private boolean isReplace = false;
    private String newPathImage = "";
    private boolean isSave = false;
    private Bitmap tmpBitmap;
    private int indexImage = -1;

    public static void startInternt(BaseActivity context, String uri, View view, boolean isReplace) {
        Intent intent = new Intent(context, EditImageActivity.class);
        intent.putExtra(URI_IMAGE, uri);
        intent.putExtra(EXTRA_REPLACE, isReplace);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_EDIT_IMAGE);
    }

    public static void startInterntWithIndex(Context context, String uri, int index, View view, boolean isReplace) {
        Intent intent = new Intent(context, EditImageActivity.class);
        intent.putExtra(URI_IMAGE, uri);
        intent.putExtra(EXTRA_REPLACE, isReplace);
        intent.putExtra(INDEX_IMAGE, index);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_EDIT_IMAGE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        enableBackButton();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image);
        mEditingToolsAdapter = new EditingToolsAdapter(this, EditImageActivity.this);

        Intent intent = getIntent();
        if (intent != null) {
            uri = intent.getStringExtra(URI_IMAGE);
            isReplace = intent.getBooleanExtra(EXTRA_REPLACE, false);
            indexImage = intent.getIntExtra(INDEX_IMAGE, -1);
        }

        if (uri == null) {
            ShowLog.ShowLog(this, binding.getRoot(), getResources().getString(R.string.error_intent), false);
            this.finish();
        }

        initViews();

        Log.e(TAG, "URI: " + uri + ", Index_Image: " + indexImage);
        binding.photoEditorView.getSource().setImageURI(Uri.parse(uri));

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();

        mPhotoEditor.setOnPhotoEditorListener(this);
    }

    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgGallery;
        ImageView imgSave;
        ImageView imgClose;

        mPhotoEditorView = binding.photoEditorView;
        mTxtCurrentTool = binding.txtCurrentTool;
        mRvTools = binding.rvConstraintTools;
        mRvFilters = binding.rvFilterView;
        mRootView = binding.rootView;

        imgUndo = binding.imgUndo;
        imgUndo.setOnClickListener(this);

        imgRedo = binding.imgRedo;
        imgRedo.setOnClickListener(this);

        imgGallery = binding.imgGallery;
        imgGallery.setOnClickListener(this);

        imgSave = binding.imgSave;
        imgSave.setOnClickListener(this);

        imgClose = binding.imgClose;
        imgClose.setOnClickListener(this);

    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode1) -> {
            final TextStyleBuilder styleBuilder = new TextStyleBuilder();
            styleBuilder.withTextColor(colorCode1);

            mPhotoEditor.editText(rootView, inputText, styleBuilder);
            mTxtCurrentTool.setText(R.string.label_text);
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.e(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.e(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.e(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.e(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.imgClose:
                this.onBackPressed();
                break;

            case R.id.imgGallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {
        newPathImage = "";
        String pathDirectory = MyPath.getPathSaveImage(this);
        File tmFile = new File(pathDirectory);
        if (!tmFile.exists()) {
            tmFile.mkdirs();
        }

        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading(getString(R.string.saving));
            String path = tmFile
                    + File.separator + ""
                    + System.currentTimeMillis() + ".png";

            File file = new File(path);
            Log.e(TAG, "Save image path: " + path);

            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                        ShowLog.ShowLog(EditImageActivity.this, binding.getRoot(), getString(R.string.save_success), true);
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(path)));
                        isSave = true;
                        newPathImage = path;
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        ShowLog.ShowLog(EditImageActivity.this, binding.getRoot(), getString(R.string.save_fail), false);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                ShowLog.ShowLog(EditImageActivity.this, binding.getRoot(), getString(R.string.save_fail), false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        tmpBitmap = bitmap.copy(bitmap.getConfig(), true);
                        mPhotoEditorView.getSource().setImageBitmap(tmpBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
        mTxtCurrentTool.setText(R.string.brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
        mTxtCurrentTool.setText(R.string.brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.ques_save_image));
        builder.setPositiveButton(getString(R.string.save), (dialog, which) -> saveImage());
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton(getString(R.string.exit), (dialog, which) -> finish());
        builder.create().show();

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.brush);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode) -> {
                    final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                    styleBuilder.withTextColor(colorCode);

                    mPhotoEditor.addText(inputText, styleBuilder);
                    mTxtCurrentTool.setText(R.string.label_text);
                });
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
                break;
            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
            case EMOJI:
                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;
        }
    }


    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        if (isSave) {
            Intent intent = new Intent();
            intent.putExtra(URI_IMAGE, uri);
            intent.putExtra(EXTRA_REPLACE, isReplace);
            intent.putExtra(PATH_IMAGE_NEW, newPathImage);
            intent.putExtra(INDEX_IMAGE, indexImage);
            setResult(Activity.RESULT_OK, intent);
        }
        super.finish();
    }
}

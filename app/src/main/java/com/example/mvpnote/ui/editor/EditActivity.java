package com.example.mvpnote.ui.editor;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mvpnote.R;
import com.example.mvpnote.data.db.Database;
import com.example.mvpnote.data.db.model.Note;
import com.example.mvpnote.utils.Const;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class EditActivity extends AppCompatActivity implements IEditActivityView, EasyImage.EasyImageStateHandler {
    private EasyImage easyImage;
    private EditText title, desc;
    private FloatingActionButton delete, done, back, addImage;
    private ImageView noteImage;
    private EditActivityPresenter editActivityPresenter;

    private Bundle easyImageState = new Bundle();

    public void starter(Context context, Note note) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Const.Note.TITLE, note.getTitle());
        intent.putExtra(Const.Note.DESC, note.getDescription());
        intent.putExtra(Const.Note.IMG_URL, note.getImageUrl());
        intent.putExtra(Const.Note.ID, note.getId());
        Log.d(TAG, "starter: " + note.getId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        back = findViewById(R.id.back);
        done = findViewById(R.id.done);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        addImage = findViewById(R.id.imageIcon);
        noteImage = findViewById(R.id.noteImg);
        delete = findViewById(R.id.removeNote);
        title.requestFocus();

        editActivityPresenter = new EditActivityPresenter(this);
        editActivityPresenter.initDB(Database.getInstance(this));

        easyImage = new EasyImage.Builder(this)
                .setChooserTitle(Const.EasyImage.TITLE)
                .setCopyImagesToPublicGalleryFolder(true)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName(Const.EasyImage.FOLDER_NAME)
                .allowMultiple(false)
                .setStateHandler(this)
                .build();

        if (savedInstanceState != null) {
            editActivityPresenter.setPhotos(savedInstanceState.getParcelableArrayList(Const.ImagePicker.PHOTOS_KEY));
            easyImageState = savedInstanceState.getParcelable(Const.ImagePicker.STATE_KEY);
        }

        bind();
        getDataFromIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Const.ImagePicker.CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openChooser(EditActivity.this);
        } else if (requestCode == Const.ImagePicker.GALLERY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openGallery(EditActivity.this);
        } else if (requestCode == Const.ImagePicker.DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(EditActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                onPhotosReturned(imageFiles);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    @Override
    public Bundle restoreEasyImageState() {
        return easyImageState;
    }

    @Override
    public void saveEasyImageState(Bundle bundle) {
        easyImageState = bundle;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.ImagePicker.PHOTOS_KEY, editActivityPresenter.getPhotos());
        outState.putParcelable(Const.ImagePicker.STATE_KEY, easyImageState);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void openGallery() {
        easyImage.openGallery(this);
    }

    @Override
    public boolean isLegacyExternalStoragePermissionRequired() {
        boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return Build.VERSION.SDK_INT < 29 && !permissionGranted;
    }

    @Override
    public void requestLegacyWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(EditActivity.this, Const.ImagePicker.LEGACY_WRITE_PERMISSIONS, Const.ImagePicker.LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    void bind() {
        title.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        desc.requestFocus();
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
        back.setOnClickListener(v -> {
            String imageUrl = "";
            if (editActivityPresenter.getPhotos().size() > 0) {
                imageUrl = editActivityPresenter.getPhotos().get(0).getFile().toString();
            }

            Note tempNote = new Note(title.getText().toString(), desc.getText().toString(), "coding", imageUrl, new Date());
            tempNote.setId(getIntent().getIntExtra(Const.Note.ID, 0));
            editActivityPresenter.onBackClicked(tempNote);
        });
        done.setOnClickListener(v -> {
            String imageUrl = "";
            if (editActivityPresenter.getPhotos().size() > 0) {
                imageUrl = editActivityPresenter.getPhotos().get(0).getFile().toString();
            }
            Note tempNote = new Note(title.getText().toString(), desc.getText().toString(), "coding", imageUrl, new Date());
            int noteId = getIntent().getIntExtra(Const.Note.ID, 0);
            //no need check for null bcs it has default value
                tempNote.setId(noteId);
                editActivityPresenter.onSaveClicked(tempNote);

        });

        addImage.setOnClickListener(v -> editActivityPresenter.onImageButtonClicked());
        delete.setOnClickListener(v -> {
            Note tempNote = new Note(title.getText().toString(), desc.getText().toString(), "coding", "", new Date());
            //no need bsc it have default value
            int noteId = getIntent().getIntExtra(Const.Note.ID, 0);
            tempNote.setId(noteId);
            editActivityPresenter.onDeleteClicked(tempNote);
        });
    }

    private void onPhotosReturned(@NonNull MediaFile[] returnedPhotos) {
        editActivityPresenter.setPhotos(new ArrayList<>(Arrays.asList(returnedPhotos)));
        Log.d(TAG, "onPhotosReturned: " + returnedPhotos[0].getFile());
        Picasso.get().load(returnedPhotos[0].getFile())
                .into(noteImage);
    }

    private void getDataFromIntent() {
        int noteId;
        String noteTitle,noteDesc,noteImageUrl;
        File imgFile;

        noteId = getIntent().getIntExtra(Const.Note.ID, 0);
        noteTitle = getIntent().getStringExtra(Const.Note.TITLE);
        noteDesc = getIntent().getStringExtra(Const.Note.DESC);
        noteImageUrl = getIntent().getStringExtra(Const.Note.IMG_URL);

        //check if it from add new or edit
        if (noteId != 0) {

            if (noteTitle ==null || noteDesc ==null|| noteImageUrl ==null){
                return;
            }
            imgFile = new File(Uri.parse(noteImageUrl).getPath());
            if (imgFile != null){
                Picasso.get().load(imgFile)
                        .into(noteImage);
            }
            title.setText(noteTitle);
            desc.setText(noteDesc);

            ArrayList<MediaFile> photos = new ArrayList<>();
            photos.add(new MediaFile(Uri.parse(noteImageUrl),imgFile ));
            editActivityPresenter.setPhotos(photos);
//
//            Log.d(TAG, "getDataFromIntent: " + getIntent().getStringExtra(Const.Note.IMG_URL));
        }


    }


}



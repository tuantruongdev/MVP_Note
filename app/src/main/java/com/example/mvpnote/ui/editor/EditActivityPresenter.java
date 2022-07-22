package com.example.mvpnote.ui.editor;


import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.mvpnote.data.db.Database;
import com.example.mvpnote.data.db.model.Note;

import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.MediaFile;

/**
 * Created by macos on 20,July,2022
 */
public class EditActivityPresenter implements IEditActivityPresenter {
    IEditActivityView iEditActivityView;
    Database db;
    ArrayList<MediaFile> photos = new ArrayList<>();

    public EditActivityPresenter(IEditActivityView iEditActivityView) {
        this.iEditActivityView = iEditActivityView;
    }

    @Override
    public void onImageButtonClicked() {
        if (iEditActivityView.isLegacyExternalStoragePermissionRequired()) {
            iEditActivityView.requestLegacyWriteExternalStoragePermission();
        } else {
            iEditActivityView.openGallery();
        }
    }

    @Override
    public void onBackClicked(Note note) {
        Log.d(TAG, "onBackClicked: " + note.getTitle() + note.getDescription());
        if ((note.getTitle().compareTo("<No title>") == 0 && note.getDescription().compareTo("<No desc>") == 0)) {
            iEditActivityView.closeActivity();
        } else {
            saveToDB(note);
            iEditActivityView.closeActivity();
        }
    }

    @Override
    public void onSaveClicked(Note note) {
        if ((note.getTitle().compareTo("<No title>") == 0 && note.getDescription().compareTo("<No desc>") == 0)) {
            iEditActivityView.showToast("you have nothing to save");
        } else {
            saveToDB(note);
            iEditActivityView.closeActivity();
        }
        Log.d(TAG, "onSaveClicked: " + note.getImageUrl());
    }

    @Override
    public void onDeleteClicked(Note note) {
        if (note.getId() == 0) {
            iEditActivityView.showToast("you not saved yet");
            return;
        }
        db.noteDao().deleteNote(note);
        iEditActivityView.closeActivity();
    }

    private void saveToDB(Note note) {
        //if this is new note
        if (note.getId() == 0) {
            db.noteDao().insertNote(note);
            Log.d(TAG, "saveToDB: created");
        } else {
            //update with id instead
            db.noteDao().updateNote(note);
            Log.d(TAG, "saveToDB: inserted");
        }
    }

    public ArrayList<MediaFile> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<MediaFile> photos) {
        this.photos = photos;
    }

    public void initDB(Database db) {
        this.db = db;
    }

}

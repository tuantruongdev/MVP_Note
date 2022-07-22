package com.example.mvpnote.ui.editor;

import com.example.mvpnote.data.db.model.Note;

/**
 * Created by macos on 20,July,2022
 */
public interface IEditActivityPresenter {
    void onImageButtonClicked();

    void onBackClicked(Note note);

    void onSaveClicked(Note note);

    void onDeleteClicked(Note note);
}

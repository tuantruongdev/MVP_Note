package com.example.mvpnote.ui.main;

import com.example.mvpnote.data.db.Database;
import com.example.mvpnote.data.db.model.Note;

import java.util.ArrayList;

/**
 * Created by macos on 20,July,2022
 */
public interface IMainActivityPresenter<V extends IMainActivityView> {
    void init(Database db);

    void getStoredNotes();

    void searchNotes(String q);

    void searchNotesByPresenter(ArrayList<Note> notes, String q);
}

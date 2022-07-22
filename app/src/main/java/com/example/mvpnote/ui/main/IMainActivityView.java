package com.example.mvpnote.ui.main;

import com.example.mvpnote.data.db.model.Note;

import java.util.ArrayList;

/**
 * Created by macos on 20,July,2022
 */
public interface IMainActivityView {
    void updateNotes(ArrayList<Note> notes);

    void updateSearchNotes(ArrayList<Note> notes);
}

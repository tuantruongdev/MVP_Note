package com.example.mvpnote.ui.main;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mvpnote.data.db.Database;
import com.example.mvpnote.data.db.model.Note;

import java.util.ArrayList;

/**
 * Created by macos on 20,July,2022
 */
public class MainActivityPresenter implements IMainActivityPresenter {
    private IMainActivityView iMainActivityView;
    private Database db;

    public MainActivityPresenter(IMainActivityView iMainActivityView) {
        this.iMainActivityView = iMainActivityView;
    }

    @Override
    public void init(Database db) {
        this.db = db;
        getStoredNotes();
    }

    @Override
    public void getStoredNotes() {
        ArrayList<Note> notes = new ArrayList<Note>(db.noteDao().getListNote());
        iMainActivityView.updateNotes(notes);
    }

    @Override
    public void searchNotes(String q) {
        ArrayList<Note> notes = new ArrayList<Note>(db.noteDao().searchNotes("%" + q + "%"));
        iMainActivityView.updateNotes(notes);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void searchNotesByPresenter(ArrayList notes, String q) {
        //convert to note model
        ArrayList<Note> myNotes = notes;
        ArrayList<Note> temp = new ArrayList<>();
        myNotes.forEach((note) -> {
            if (note.getTitle().contains(q) || note.getDescription().contains(q)) {
                temp.add(note);
            }
        });
        iMainActivityView.updateSearchNotes(temp);
    }

}

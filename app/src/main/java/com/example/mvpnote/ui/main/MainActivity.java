package com.example.mvpnote.ui.main;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mvpnote.R;
import com.example.mvpnote.data.db.Database;
import com.example.mvpnote.data.db.model.Note;
import com.example.mvpnote.ui.editor.EditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainActivityView {
    RecyclerView recyclerView;
    MainActivityAdapter mainActivityAdapter;
    EditText search;
    FloatingActionButton add;
    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.addNote);
        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recycle_view_main);
        mainActivityAdapter = new MainActivityAdapter();

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mainActivityAdapter);
        //addTempData();
        mainActivityPresenter = new MainActivityPresenter(this);
        mainActivityPresenter.init(Database.getInstance(this));

        bind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNote:
                new EditActivity().starter(this, new Note("", "", "", "", new Date()));
        }
    }

    @Override
    public void updateNotes(ArrayList<Note> notes) {
        mainActivityAdapter.setData(notes);
    }

    @Override
    public void updateSearchNotes(ArrayList<Note> notes) {
        mainActivityAdapter.setSearchData(notes);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resumed");
        if (search.getText().toString().compareTo("") == 0) {
            mainActivityPresenter.getStoredNotes();
        } else {
            //presenter search
            // mainActivityPresenter.searchNotesByPresenter(mainActivityAdapter.getStoredData(),search.getText().toString());
            //sql search
             mainActivityPresenter.searchNotes(search.getText().toString());
        }
    }

    private void bind() {
        add.setOnClickListener(this);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //presenter search
                //   mainActivityPresenter.searchNotesByPresenter(mainActivityAdapter.getStoredData(),search.getText().toString());
                //sql search
                mainActivityPresenter.searchNotes(search.getText().toString());
                Log.d(TAG, "onTextChanged: searching");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addTempData() {
        ArrayList<Note> notes = new ArrayList<>();
        Database instance = Database.getInstance(this);
        instance.noteDao().insertNote(new Note("title", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed", "coding", "", new Date()));
        instance.noteDao().insertNote(new Note("title1", "description", "coding", ".", new Date()));
        instance.noteDao().insertNote(new Note("title2 lorem ispum xd xd aa ga ", "description", "coding", "", new Date()));
        instance.noteDao().insertNote(new Note("title3", "description", "coding", ".", new Date()));
        instance.noteDao().insertNote(new Note("title4", "description", "coding", "", new Date()));
        instance.noteDao().insertNote(new Note("title5", "description", "coding", ".", new Date()));
        instance.noteDao().insertNote(new Note("title6", "description", "coding", "", new Date()));
    }
}
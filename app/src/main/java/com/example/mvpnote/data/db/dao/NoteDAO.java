package com.example.mvpnote.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvpnote.data.db.model.Note;

import java.util.List;

/**
 * Created by macos on 19,July,2022
 */
@Dao
public interface NoteDAO {

    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM notes")
    List<Note> getListNote();

    @Query("SELECT * FROM notes WHERE title like :q or description like :q")
    List<Note> searchNotes(String q);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

}

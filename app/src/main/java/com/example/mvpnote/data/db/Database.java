package com.example.mvpnote.data.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mvpnote.data.db.dao.NoteDAO;
import com.example.mvpnote.data.db.model.Note;
import com.example.mvpnote.utils.Const;

/**
 * Created by macos on 19,July,2022
 */
@androidx.room.Database(entities = {Note.class}, version = 69)
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, Database.class, Const.Database.DB_NAME)
                    .build();
        }
        return instance;
    }

    public abstract NoteDAO noteDao();
}

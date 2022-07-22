package com.example.mvpnote.data.db.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mvpnote.utils.Const;
import com.example.mvpnote.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by macos on 19,July,2022
 */
@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String tag;
    private String imageUrl;
    @TypeConverters(DateConverter.class)
    private Date createAt;

    public Note(String title, String description, String tag, String imageUrl, Date createAt) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        if (title.compareTo("") == 0) {
            return "<No title>";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (description.compareTo("") == 0) {
            return "<No desc>";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getNoteType() {
        if (imageUrl == null || imageUrl.compareTo("") == 0) {
            return Const.ViewType.NOTE_TYPE_NORMAL;
        }
        return Const.ViewType.NOTE_TYPE_IMG;
    }

    public Long getTimeStamp() {
        try {
            Date current = new Date();
            return current.getTime() / 1000 - createAt.getTime() / 1000;
        } catch (Exception j) {
            j.printStackTrace();
        }
        return Long.valueOf(0);
    }

    public String toDuration() {
        List<Long> times = new ArrayList<>();
        times.add(TimeUnit.DAYS.toMillis(365) / 1000);
        times.add(TimeUnit.DAYS.toMillis(30) / 1000);
        times.add(TimeUnit.DAYS.toMillis(1) / 1000);
        times.add(TimeUnit.HOURS.toMillis(1) / 1000);
        times.add(TimeUnit.MINUTES.toMillis(1) / 1000);
        times.add(TimeUnit.SECONDS.toMillis(1) / 1000);
        List<String> timesString = new ArrayList<>();

        timesString.add("y");
        timesString.add("mh");
        timesString.add("d");
        timesString.add("h");
        timesString.add("m");
        timesString.add("s");

        long duration = getTimeStamp();
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append("").append(timesString.get(i)).append(temp != 1 ? "" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 seconds ago";
        else
            Log.d(TAG, "toDuration: " + res);
        return res.toString();
    }


}

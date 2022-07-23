package com.example.mvpnote.ui.main;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvpnote.R;
import com.example.mvpnote.data.db.model.Note;
import com.example.mvpnote.ui.editor.EditActivity;
import com.example.mvpnote.utils.Const;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by macos on 19,July,2022
 */
public class MainActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private ArrayList<Note> notes, storedNotes;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Const.ViewType.NOTE_TYPE_IMG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_img, parent, false);
            return new NoteImgViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note note = notes.get(position);
        if (holder.getItemViewType() == Const.ViewType.NOTE_TYPE_IMG) {
            NoteImgViewHolder mHolder = (NoteImgViewHolder) holder;
            mHolder.noteTitle.setText(limitCharacter(note.getTitle(), Const.Note.TITLE_LIMIT));
            mHolder.noteDes.setText(limitCharacter(note.getDescription(), Const.Note.DESC_LIMIT));

            File img = new File(Uri.parse(note.getImageUrl()).getPath());

            if (img != null) {
                Picasso.get().load(img)
                        .into(mHolder.noteImg);
            }

            Log.d(TAG, "onBindViewHolder: " + note.getImageUrl());
            // mHolder.noteImg.setImageResource(R.drawable.dummy_img);
            mHolder.noteTime.setText(note.toDuration());
            mHolder.noteTag.setText("Coding");
            mHolder.setItemClickListener((view, position1, isLongClick) -> {
                Log.d(TAG, "click: ");
                new EditActivity().starter(view.getContext(), note);
            });
        } else {
            NoteViewHolder mHolder = (NoteViewHolder) holder;
            mHolder.noteTitle.setText(limitCharacter(note.getTitle(), Const.Note.TITLE_LIMIT));
            mHolder.noteDes.setText(limitCharacter(note.getDescription(), Const.Note.DESC_LIMIT));
            mHolder.noteTime.setText(note.toDuration());
            mHolder.noteTag.setText("Coding");

            mHolder.setItemClickListener((view, position1, isLongClick) -> {
                Log.d(TAG, "click: ");
                if (isLongClick) {
                    Log.d(TAG, "onBindViewHolder: long click");

                }
                new EditActivity().starter(view.getContext(), note);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Note note = notes.get(position);
        if (note.getNoteType() == Const.ViewType.NOTE_TYPE_IMG) {
            return Const.ViewType.NOTE_TYPE_IMG;
        }
        return Const.ViewType.NOTE_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

    public void setSearchData(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public ArrayList<Note> getStoredData() {
        return this.storedNotes;
    }

    public ArrayList<Note> getData() {
        return this.notes;
    }

    public void setData(ArrayList<Note> notes) {
        this.notes = notes;
        this.storedNotes = notes;
        //    Log.d(TAG, "setData:" +notes.get(0).getId());
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String limitCharacter(String raw, long limit) {
        long rawCount = raw.chars().count();
        if (limit > rawCount) {
            limit = rawCount;
            return raw.substring(0, Math.toIntExact(limit));
        }
        return raw.substring(0, Math.toIntExact(limit)) + "...";
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, boolean isLongClick);
    }

    public class NoteImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView noteTitle, noteDes, noteTag, noteTime;
        ImageView noteImg;
        private ItemClickListener itemClickListener;

        public NoteImgViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDes = itemView.findViewById(R.id.note_description);
            noteTag = itemView.findViewById(R.id.note_tag);
            noteTime = itemView.findViewById(R.id.note_time);
            noteImg = itemView.findViewById(R.id.note_img);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition(), false);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView noteTitle, noteDes, noteTag, noteTime;
        private ItemClickListener itemClickListener;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDes = itemView.findViewById(R.id.note_description);
            noteTag = itemView.findViewById(R.id.note_tag);
            noteTime = itemView.findViewById(R.id.note_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition(), true);
            return true;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


    }

}

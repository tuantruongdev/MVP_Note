package com.example.mvpnote.utils;

import android.Manifest;

/**
 * Created by macos on 19,July,2022
 */
public interface Const {
    interface ViewType {
        int NOTE_TYPE_NORMAL = 1;
        int NOTE_TYPE_IMG = 2;
    }

    interface Database {
        String DB_NAME = "noteApp";
        String TABLE_NAME = "notes";
    }

    interface ImagePicker {
        String PHOTOS_KEY = "easy_image_photos_list";
        String STATE_KEY = "easy_image_state";
        int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
        int GALLERY_REQUEST_CODE = 7502;
        int DOCUMENTS_REQUEST_CODE = 7503;
        int LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 456;
        String[] LEGACY_WRITE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    interface Note {
        String TITLE = "note_title";
        String DESC = "note_desc";
        String IMG_URL = "note_image_url";
        String ID = "note_id";
        int TITLE_LIMIT = 128;
        int DESC_LIMIT = 256;
    }

    interface EasyImage {
        String TITLE = "Pick Media";
        String FOLDER_NAME = "EasyImage sample";
    }
}

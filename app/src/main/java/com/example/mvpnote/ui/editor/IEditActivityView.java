package com.example.mvpnote.ui.editor;

/**
 * Created by macos on 20,July,2022
 */
public interface IEditActivityView {
    void closeActivity();

    void openGallery();

    boolean isLegacyExternalStoragePermissionRequired();

    void requestLegacyWriteExternalStoragePermission();

    void showToast(String text);
}

package com.example.adm.myapplication;

import android.graphics.Bitmap;

public class NotificationEvent {

    private Bitmap bitmap;

    NotificationEvent(Bitmap bitmap) {this.bitmap = bitmap;}

    public Bitmap getBody(){return bitmap;}
}

package com.example.lam.numbers.models;


import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Lam on 11/8/2015.
 */
public class ColorItem implements Serializable {
    private int color;
    private int player;

    private ColorItem() {
    }

    public ColorItem(int color, int player) {
        this.color = color;
        this.player = player;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}

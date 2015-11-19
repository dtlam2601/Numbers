package com.example.lam.numbers.models;

import java.io.Serializable;

/**
 * Created by Lam on 11/12/2015.
 */
public class NumberItem implements Serializable {
    private int number;
    private int flag;

    public NumberItem(int number, int flag) {
        this.number = number;
        this.flag = flag;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}

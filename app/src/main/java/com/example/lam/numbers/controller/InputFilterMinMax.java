package com.example.lam.numbers.controller;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

/**
 * Created by Lam on 11/9/2015.
 */
public class InputFilterMinMax implements InputFilter {
    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            Log.d("InputFilter", "source " + source + ", start " + String.valueOf(start) + ", end " + String.valueOf(end)
                    + ", dest " + String.valueOf(dest) + ", dstart " + String.valueOf(dstart) + ", dend " + String.valueOf(dend));
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input)) {
                return null;
            }
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int min, int max, int input) {
        return (max > min) ? (input >= min && input <= max) : (input >= max || input <= min);
    }
}

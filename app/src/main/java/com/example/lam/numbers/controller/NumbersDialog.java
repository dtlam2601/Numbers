package com.example.lam.numbers.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.lam.numbers.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lam on 11/10/2015.
 */
public class NumbersDialog extends DialogFragment {
    private static final String TAG = "NumbersDialog";
    private NumbersDialogListener listener;

    public interface NumbersDialogListener {
        void onNumbersDialogPositiveClick(String number);

        void onNumbersDialogNegativeClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (NumbersDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] numberArray = {"25", "50"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.number_picker_title)
                .setItems(numberArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNumbersDialogPositiveClick(numberArray[which]);
                    }
                });

        return builder.create();
    }

    class ViewHolder {

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void Bind(final String[] numberArray) {
        }
    }
}

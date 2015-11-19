package com.example.lam.numbers.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lam.numbers.R;
import com.example.lam.numbers.models.Info;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lam on 11/12/2015.
 */
public class MessageDialog extends DialogFragment {
    private static final String KEY_INFO = "info";
    private MessageDialogListener listener;
    private Info info;

    public interface MessageDialogListener {
        void onMessageDialogRestartGameClick();
    }

    public static DialogFragment newInstance(Info info) {
        MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putSerializable(KEY_INFO, info);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (MessageDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
        info = (Info) getArguments().getSerializable(KEY_INFO);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.notice_message_player_layout, null);
        ViewHolder holder = new ViewHolder(view);
        holder.Bind();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.number_picker_title)
                .setView(view);

        builder.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onMessageDialogRestartGameClick();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MessageDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    class ViewHolder {
        @Bind(R.id.tv_player_1)
        TextView tvPlayer1;
        @Bind(R.id.tv_player_2)
        TextView tvPlayer2;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void Bind() {
            tvPlayer1.setText(getString(R.string.msg_player1, info.getPlayer1()));
            tvPlayer2.setText(getString(R.string.msg_player2, info.getPlayer2()));
        }
    }
}

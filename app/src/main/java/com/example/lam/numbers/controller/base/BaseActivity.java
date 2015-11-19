package com.example.lam.numbers.controller.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Lam on 11/10/2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void setContentView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
    }

}

package com.example.lam.numbers.controller;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.example.lam.numbers.R;
import com.example.lam.numbers.adapter.GridViewNumbersAdapter;
import com.example.lam.numbers.controller.base.BaseActivity;
import com.example.lam.numbers.models.Info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

/**
 * Created by Lam on 11/10/2015.
 */
public class NumbersGameActivity extends BaseActivity implements MessageDialog.MessageDialogListener,
        GridViewNumbersAdapter.GridViewNumbersAdapterListener {
    private static final String TAG = "NumbersGamesActivity";
    private static final String KEY_INFO = "info";

    @Bind(R.id.tv_numbers)
    TextView tvNumbers;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.rl_left_member)
    RelativeLayout rlLeftMember;
    @Bind(R.id.rl_right_member)
    RelativeLayout rlRightMember;
    @Bind(R.id.tv_left_member)
    TextView tvLeftMember;
    @Bind(R.id.tv_right_member)
    TextView tvRightMember;
    @Bind(R.id.gv_numbers)
    GridView gvNumbers;
    @Bind(R.id.pb_loading)
    ProgressBar pbLoading;
    @Bind(R.id.tv_left_counter)
    TextView tvLeftCounter;
    @Bind(R.id.tv_right_counter)
    TextView tvRightCounter;

    private Random rand;
    private int numbers;
    private int randNumber;
    private int position = 0;
    private boolean isEntered = false;
    private List<Integer> arrayNumbers;
    private List<Integer> arrayRandomNumbers;
    private List<Integer> arrayLeftNumbers;
    private List<Integer> arrayRightNumbers;
    private GridViewNumbersAdapter numbersAdapter;
    private MediaPlayer mp;

    public static Intent newInstance(Context context, Info info) {
        Log.d(TAG, "number " + String.valueOf(info.getNumber()));
        Log.d(TAG, "color1 " + String.valueOf(info.getPlayer1()));
        Log.d(TAG, "color2 " + String.valueOf(info.getPlayer2()));

        Intent intent = new Intent(context, NumbersGameActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(KEY_INFO, info);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.numbers_game_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initUI();
    }

    //----------------------------------------------------------------------------------------------
    //  Setup
    //----------------------------------------------------------------------------------------------

    private void initData() {
        rand = new Random();
        Bundle args = getIntent().getExtras();
        if (args == null)
            return;

        Info info = (Info) args.getSerializable(KEY_INFO);
        int color1 = info.getPlayer1();
        int color2 = info.getPlayer2();
        this.numbers = info.getNumber();
        tvLeft.setTextColor(color1);
        tvLeftMember.setBackgroundColor(color1);
        tvLeftCounter.setTextColor(color1);

        tvRight.setTextColor(color2);
        tvRightMember.setBackgroundColor(color2);
        tvRightCounter.setTextColor(color2);

        //random number
        arrayLeftNumbers = new ArrayList<>();
        arrayRightNumbers = new ArrayList<>();
        arrayNumbers = new ArrayList<>();
        arrayRandomNumbers = new ArrayList<>();
        for (int i = 1; i <= numbers; i++) {
            arrayNumbers.add(i);
            arrayRandomNumbers.add(i);
        }
        Collections.shuffle(arrayNumbers);
        Collections.shuffle(arrayRandomNumbers);
    }

    private void initUI() {
        //gv number
        initGridViewNumbers();
        //tv number
        setRandNumbers();
        //left player
        rlLeftMember.setOnDragListener(new onDragListener());
        tvLeftMember.setOnDragListener(new onDragListener());
        //right player
        rlRightMember.setOnDragListener(new onDragListener());
        tvRightMember.setOnDragListener(new onDragListener());
    }

    private void initGridViewNumbers() {
        int width;
        int height;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels - dpToPx(120);
        height = metrics.heightPixels - dpToPx(80);

        int numColumns = 5;
        if (numbers == 25)
            numColumns = 4;
        int numRows = numbers / numColumns;
        int columnWidth = width / numColumns;
        int columnHeight = height / numRows;

        int sizeItem = columnHeight;
        if (columnWidth < columnHeight)
            sizeItem = columnWidth;

        gvNumbers.setNumColumns(numColumns);
        gvNumbers.setColumnWidth(sizeItem);

        numbersAdapter = new GridViewNumbersAdapter(this, arrayNumbers, sizeItem - 20);
        gvNumbers.setAdapter(numbersAdapter);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    private void setRandNumbers() {
        if (position > arrayNumbers.size())
            return;
        loading();
        if (position == arrayNumbers.size()) {
            tvNumbers.setText(String.valueOf(0));
            numbersAdapter.setRandNumber(0);
            Info info = new Info(0, arrayLeftNumbers.size(), arrayRightNumbers.size());
            MessageDialog msgDialog = (MessageDialog) new MessageDialog().newInstance(info);
            msgDialog.show(getSupportFragmentManager(), "NumbersGameActivity");
            return;
        }
        randNumber = arrayRandomNumbers.get(position);
        tvNumbers.setText(String.valueOf(randNumber));
        numbersAdapter.setRandNumber(randNumber);
        position++;
    }

    private void loading() {
        final float percent = (float) position / (float) numbers * 100;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                pbLoading.setProgress((int) percent);
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    // Event
    //----------------------------------------------------------------------------------------------

    private void setEnabledFalse(TextView view) {
        view.setTextColor(getResources().getColor(R.color.light_grey));
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setAlpha(50);
        Log.d(TAG, "TextView " + view.getText() + " - " + view.getAlpha());
    }

    @Override
    public void setAlphaGridViewNumbers(float alpha) {
        gvNumbers.setAlpha(alpha);
    }

    private class onDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_EXITED:
                    if (!isEntered)
                        return false;
                    isEntered = false;
                    if (view.getId() == rlLeftMember.getId() || view.getId() == tvLeftMember.getId())
                        animationPlayerAreaCollapse(tvLeftMember);
                    if (view.getId() == rlRightMember.getId() || view.getId() == tvRightMember.getId())
                        animationPlayerAreaCollapse(tvRightMember);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (isEntered)
                        return false;
                    isEntered = true;
                    if (view.getId() == rlLeftMember.getId() || view.getId() == tvLeftMember.getId())
                        animationPlayerAreaExpand(tvLeftMember);
                    if (view.getId() == rlRightMember.getId() || view.getId() == tvRightMember.getId())
                        animationPlayerAreaExpand(tvRightMember);
                    break;
                case DragEvent.ACTION_DROP:
                    isEntered = false;
                    if (view.getId() == tvLeftMember.getId() || view.getId() == rlLeftMember.getId()) {
                        animationPlayerAreaCollapse(tvLeftMember);
                        arrayLeftNumbers.add(randNumber);
                        updateCounter(tvLeftCounter, arrayLeftNumbers.size());
                        goalSound();
                    }

                    if (view.getId() == tvRightMember.getId() || view.getId() == rlRightMember.getId()) {
                        animationPlayerAreaCollapse(tvRightMember);
                        arrayRightNumbers.add(randNumber);
                        updateCounter(tvRightCounter, arrayRightNumbers.size());
                        goalSound();
                    }

                    setEnabledFalse((TextView) event.getLocalState());
                    setRandNumbers();
                    break;
            }
            return true;
        }
    }

    private void updateCounter(TextView textView, int count) {
        String countText = String.valueOf(count);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_counter);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_counter);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(slideOut);
        set.addAnimation(slideIn);
        textView.startAnimation(slideOut);
        textView.setText(countText);
        textView.startAnimation(slideIn);
    }

    private void goalSound() {
        mp = MediaPlayer.create(this, R.raw.goal_sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    private void animationPlayerAreaExpand(final View view) {
        int start = view.getMeasuredWidth();
        int end = start + 30;
        final ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = value;
                view.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    private void animationPlayerAreaCollapse(final View view) {
        int start = view.getMeasuredWidth();
        int end;
        if (view.getId() == tvLeftMember.getId())
            end = tvRightMember.getMeasuredWidth();
        else
            end = tvLeftMember.getMeasuredWidth();
        final ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = value;
                view.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    private void reset() {
        position = 0;
        arrayLeftNumbers.clear();
        arrayRightNumbers.clear();
        Collections.shuffle(arrayNumbers);
        Collections.shuffle(arrayRandomNumbers);
        //gv number
        initGridViewNumbers();
        //tv number
        setRandNumbers();
    }

    @Override
    public void onMessageDialogRestartGameClick() {
        reset();
    }
}

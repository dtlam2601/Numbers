package com.example.lam.numbers.controller;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lam.numbers.R;
import com.example.lam.numbers.adapter.GridViewColorPaletteAdapter;
import com.example.lam.numbers.adapter.GridViewColorPaletteAdapter.GridViewColorPaletteListener;
import com.example.lam.numbers.controller.base.BaseActivity;
import com.example.lam.numbers.models.ColorItem;
import com.example.lam.numbers.models.Info;
import com.example.lam.numbers.services.SharedPreference;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements GridViewColorPaletteListener,
        NumbersDialog.NumbersDialogListener {
    private static final String SHOWCASE_ID = "showcase";

    @Bind(R.id.tv_left_color)
    TextView tvLeftColor;
    @Bind(R.id.tv_right_color)
    TextView tvRightColor;
    @Bind(R.id.tv_left_text)
    TextView tvLeftText;
    @Bind(R.id.tv_left_text_first)
    TextView tvLeftTextFirst;
    @Bind(R.id.tv_right_text)
    TextView tvRightText;
    @Bind(R.id.tv_right_text_first)
    TextView tvRightTextFirst;
    @Bind(R.id.tv_numbers)
    TextView tvNumbers;
    @Bind(R.id.gv_color_palette)
    GridView gvColorPalette;
    @Bind(R.id.rl_root_view)
    RelativeLayout rootView;
    @Bind(R.id.btn_start)
    Button btnStart;
    @Bind(R.id.tv_choice_color)
    TextView tvChoiceColor;

//    private TextView tvChoiceColor;
    private int idTVChoiceColor = -1;
    private List<ColorItem> arrayColor;
    private SharedPreference sharedPref;
    private GridViewColorPaletteAdapter colorPaletteAdapter;
    private MediaPlayer mp;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initUI();
    }

    //----------------------------------------------------------------------------------------------
    // SETUP
    //----------------------------------------------------------------------------------------------

    private void initData() {
        // colors palette
        arrayColor = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            int color = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
            arrayColor.add(new ColorItem(color, 0));
        }
    }

    private void initUI() {
        colorPaletteAdapter = new GridViewColorPaletteAdapter(this,arrayColor);
        gvColorPalette.setAdapter(colorPaletteAdapter);

        sharedPref = new SharedPreference(this);

        gvColorPalette.post(new Runnable() {
            @Override
            public void run() {
                int isFirst = sharedPref.read(getString(R.string.key_is_first), 1);
                if (isFirst == 0) {
                    showMain();
                    return;
                }
                showFirst();
            }
        });
    }

    private void showFirst() {
        btnStart.setEnabled(false);
        tvNumbers.setVisibility(View.INVISIBLE);
        showLeftMember();
    }

    private void showLeftMember() {
        Handler handler = new Handler();
        setAnimationSlide(tvLeftColor, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAnimatedColor(tvLeftTextFirst);
                tvChoiceColor.setVisibility(View.VISIBLE);
                tvChoiceColor.setText(getString(R.string.choice_color_left));
                goalSound();
            }
        }, 500);
    }

    private void showRightMember() {
        Handler handler = new Handler();
        setAnimationSlide(tvRightColor, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvRightColorClick();
                tvLeftTextFirst.setVisibility(View.INVISIBLE);
                tvLeftText.setVisibility(View.VISIBLE);
                setAnimatedColor(tvRightTextFirst);
                tvChoiceColor.setVisibility(View.VISIBLE);
                tvChoiceColor.setText(getString(R.string.choice_color_right));
                goalSound();
            }
        }, 500);
    }

    private void showMain() {
        tvLeftText.setVisibility(View.VISIBLE);
        tvRightText.setVisibility(View.VISIBLE);
        tvLeftColor.setVisibility(View.VISIBLE);
        tvRightColor.setVisibility(View.VISIBLE);

        int pos1 = sharedPref.read(getString(R.string.key_color_1), Integer.parseInt(getString(R.string.key_color_1_default)));
        int pos2 = sharedPref.read(getString(R.string.key_color_2), Integer.parseInt(getString(R.string.key_color_2_default)));
        tvLeftColor.setBackgroundColor(arrayColor.get(pos1).getColor());
        tvRightColor.setBackgroundColor(arrayColor.get(pos2).getColor());
        arrayColor.get(pos1).setPlayer(1);
        arrayColor.get(pos2).setPlayer(2);
        setAnimationColorDown(pos1, 0);
        setAnimationColorDown(pos2, 0);
    }

    //----------------------------------------------------------------------------------------------
    // EVENT
    //----------------------------------------------------------------------------------------------

    @OnClick(R.id.tv_left_color)
    protected void tvLeftColorClick() {
        colorPaletteAdapter.setPlayer(1);
    }

    @OnClick(R.id.tv_right_color)
    protected void tvRightColorClick() {
        colorPaletteAdapter.setPlayer(2);
    }

    @OnClick(R.id.tv_numbers)
    protected void tvNumberClick() {
        NumbersDialog dialog = new NumbersDialog();
        dialog.show(getSupportFragmentManager(), "NumbersDialog");
    }

    @Override
    public void onGridViewColorClick(int player, int position) {
        if (arrayColor == null || arrayColor.size() == 0)
            return;
        for (int i = 0; i < arrayColor.size(); i++) {
            if (arrayColor.get(i).getPlayer() == player) {
                Log.d("MainActivity", "i " + String.valueOf(i));
                Log.d("GridView", "Main position " + String.valueOf(i));
                arrayColor.get(i).setPlayer(0);
                setAnimationColorUp(i);
            }
        }
        arrayColor.get(position).setPlayer(player);
        colorPaletteAdapter.notifyData(arrayColor);
        if (player == 1) {
            tvLeftColor.setBackgroundColor(arrayColor.get(position).getColor());
            sharedPref.write(getString(R.string.key_color_1), position);
        }
        if (player == 2) {
            tvRightColor.setBackgroundColor(arrayColor.get(position).getColor());
            sharedPref.write(getString(R.string.key_color_2), position);
        }
        setAnimationColorDown(position, -1);

        //check isFirstShow
        int isFirst = sharedPref.read(getString(R.string.key_is_first), 1);
        if (isFirst == 0)
            return;
        tvChoiceColor.setVisibility(View.INVISIBLE);
        if (tvRightColor.getVisibility() != View.VISIBLE) {
            showRightMember();
            return;
        }
        tvRightTextFirst.setVisibility(View.INVISIBLE);
        tvRightText.setVisibility(View.VISIBLE);
        tvNumbers.setVisibility(View.VISIBLE);
        btnStart.setEnabled(true);
        sharedPref.write(getString(R.string.key_is_first), 0);
    }


    private void setAnimationSlide(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        int oldWidth = 0;
        int newWidth = (int) getResources().getDimension(R.dimen.width_color_bar);
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(oldWidth, newWidth);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                Log.d("Tag", "value " + value);

                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = value;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    private void setAnimatedColor(final TextView textView) {
        textView.setVisibility(View.VISIBLE);
        AnimatedColorSpan span = new AnimatedColorSpan(this);
        String text = textView.getText().toString();

        final SpannableString spannableString = new SpannableString(text);
        int start = 0;
        int end = start + text.length();
        spannableString.setSpan(span, start, end, 0);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                span, span.ANIMATED_COLOR_SPAN_FLOAT_PROPERTY, 0, 100);
        objectAnimator.setEvaluator(new FloatEvaluator());
        objectAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        textView.setText(spannableString);
                    }
                }
        );

        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(DateUtils.MINUTE_IN_MILLIS * 3);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    private void setAnimationColorUp(int position) {
        View view = gvColorPalette.getChildAt(position);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation_scale_up);
        view.startAnimation(anim);
    }

    private void setAnimationColorDown(int position, int duration) {
        View view = gvColorPalette.getChildAt(position);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation_scale_down);
        if (duration == 0)
            anim.setDuration(duration);
        view.startAnimation(anim);
    }

    @Override
    public void onNumbersDialogPositiveClick(String number) {
        tvNumbers.setText(number);
    }

    @Override
    public void onNumbersDialogNegativeClick() {

    }

    @OnClick(R.id.btn_start)
    protected void navigationNumbersGame() {
        Info info = new Info(Integer.parseInt(tvNumbers.getText().toString()),
                ((ColorDrawable) tvLeftColor.getBackground()).getColor(),
                ((ColorDrawable) tvRightColor.getBackground()).getColor());
        Intent intent = NumbersGameActivity.newInstance(this, info);
        startActivity(intent);
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
}

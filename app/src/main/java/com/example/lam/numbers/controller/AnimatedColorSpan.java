package com.example.lam.numbers.controller;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.util.Property;

import com.example.lam.numbers.R;

/**
 * Created by Lam on 11/14/2015.
 */
public class AnimatedColorSpan extends CharacterStyle {
    private int[] colors;
    private Shader shader = null;
    private Matrix matrix = new Matrix();
    private float translateXPercentage = 0;

    public AnimatedColorSpan(Context context) {
        colors = context.getResources().getIntArray(R.array.rainbow);
    }

    public void setTranslateXPercentage(float percentage) {
        this.translateXPercentage = percentage;
    }

    public float getTranslateXPercentage() {
        return translateXPercentage;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setStyle(Paint.Style.FILL);
        float width = textPaint.getTextSize() * colors.length;
        if (shader == null) {
            shader = new LinearGradient(0, 0, 0, width, colors, null, Shader.TileMode.MIRROR);
        }
        matrix.reset();
        matrix.setRotate(90);
        matrix.postTranslate(width * translateXPercentage, 0);
        shader.setLocalMatrix(matrix);
        textPaint.setShader(shader);
    }

    public static final Property<AnimatedColorSpan, Float> ANIMATED_COLOR_SPAN_FLOAT_PROPERTY =
            new Property<AnimatedColorSpan, Float>(Float.class,
                    "ANIMATED_COLOR_SPAN_FLOAT_PROPERTY") {
                @Override
                public void set(AnimatedColorSpan span, Float value) {
                    span.setTranslateXPercentage(value);
                }

                @Override
                public Float get(AnimatedColorSpan span) {
                    return span.getTranslateXPercentage();
                }
            };
}
package com.example.lam.numbers.adapter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lam.numbers.R;
import com.example.lam.numbers.adapter.base.BaseAdapter;
import com.example.lam.numbers.models.NumberItem;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lam on 11/10/2015.
 */
public class GridViewNumbersAdapter extends BaseAdapter<Integer> {
    private static final String TAG = "GridViewNumbersAdapter";
    private GridViewNumbersAdapterListener listener;
    private int randNumber;
    private int sizeItem;

    public interface GridViewNumbersAdapterListener {
        void setAlphaGridViewNumbers(float alpha);
    }

    public GridViewNumbersAdapter(Context context, List<Integer> array, int sizeItem) {
        super(context, array);
        this.sizeItem = sizeItem;
        this.listener = (GridViewNumbersAdapterListener) context;
    }

    public void setRandNumber(int randNumber) {
        this.randNumber = randNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_number_layout, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.Bind(mArray.get(position));
        return convertView;
    }

    private RelativeLayout.LayoutParams params;

    class ViewHolder {
//        @Bind(R.id.root_view)
//        RelativeLayout rootView;
        @Bind(R.id.tv_rand_number)
        TextView tvRandNumber;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void Bind(final Integer number) {
            if (number == null)
                return;

            tvRandNumber.setText(String.valueOf(number));
            ViewGroup.LayoutParams params = tvRandNumber.getLayoutParams();
            Log.d(TAG, "params " + params.width + " - " + params.height);
            params.width = sizeItem;
            params.height = sizeItem;
            tvRandNumber.setLayoutParams(params);

            tvRandNumber.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (randNumber != number)
                        return false;
                    return true;
                }
            });

            tvRandNumber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (randNumber != number)
                        return false;
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText("", "");
                        ShadowBuilder shadowBuilder = new ShadowBuilder(tvRandNumber);
                        Log.d("GridViewNumbers", "Adapter position " + String.valueOf(view.getTag()));
                        tvRandNumber.startDrag(data, shadowBuilder, tvRandNumber, 0);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            final View rootView = tvRandNumber.getRootView();
            tvRandNumber.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            if(rootView instanceof GridView)
//                            rootView.setAlpha(0.5f);
                            listener.setAlphaGridViewNumbers(0.5f);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
//                            rootView.setAlpha(1f);
                            listener.setAlphaGridViewNumbers(1f);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    class ShadowBuilder extends View.DragShadowBuilder {
        private final WeakReference<View> mView;

        public ShadowBuilder(View view) {
            super(view);
            mView = new WeakReference<>(view);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.scale(1.5f, 1.5f);
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            final View view = mView.get();
            if (view != null) {
                shadowSize.set((int) (view.getWidth() * 1.5F), (int) (view.getHeight() * 1.5F));
                shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
            }
        }
    }
}

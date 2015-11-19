package com.example.lam.numbers.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.lam.numbers.R;
import com.example.lam.numbers.adapter.base.BaseAdapter;
import com.example.lam.numbers.models.ColorItem;
import com.example.lam.numbers.services.SharedPreference;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lam on 11/8/2015.
 */
public class GridViewColorPaletteAdapter extends BaseAdapter<ColorItem> {
    private static final String TAG = "GridViewColorPalette";
    private GridViewColorPaletteListener listener;
    private int mPlayer = 1;

    public interface GridViewColorPaletteListener {
        void onGridViewColorClick(int player, int colorPosition);
    }

    public GridViewColorPaletteAdapter(Context context, List<ColorItem> dataSet) {
        super(context, dataSet);
        listener = (GridViewColorPaletteListener) context;
    }

    public void notifyData(List<ColorItem> colorItems) {
        mArray = colorItems;
    }

    public void setPlayer(int player) {
        this.mPlayer = player;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_color_layout, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.Bind(mArray.get(position), position);
        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.tv_color)
        TextView tvColor;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void Bind(ColorItem item, final int position) {
            if(item == null)
                return;

            GradientDrawable drawable = (GradientDrawable) tvColor.getBackground();
            if(drawable == null)
                drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(item.getColor());
            tvColor.setBackground(drawable);

            tvColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mArray.get(position).getPlayer() > 0) {
                        Log.d(TAG, "position " + String.valueOf(position) + " - " + mArray.get(position).getPlayer());
                        return;
                    }
                    if(listener != null) {
                        listener.onGridViewColorClick(mPlayer, position);
                    }
                }
            });
        }
    }
}

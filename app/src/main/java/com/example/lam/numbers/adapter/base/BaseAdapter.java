package com.example.lam.numbers.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lam on 11/8/2015.
 */
public class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context mContext;
    protected List<T> mArray;

    public BaseAdapter(Context context, List<T> array) {
        this.mContext = context;
        this.mArray = array;
    }

    @Override
    public int getCount() {
        if (mArray == null)
            return 0;
        return mArray.size();
    }

    @Override
    public T getItem(int position) {
        if (mArray == null || mArray.size() == 0)
            return null;
        return mArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mArray == null)
            return 0;
        return mArray.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}

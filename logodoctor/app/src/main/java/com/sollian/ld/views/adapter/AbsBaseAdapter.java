package com.sollian.ld.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by sollian on 2015/9/18.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    protected List<T> dataSource;

    public AbsBaseAdapter(Context context, List<T> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public T getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutResId(), null);
            holder = getHolder();
            holder.onBindView(convertView);
        } else {
            holder = (IHolder) convertView.getTag();
        }
        holder.onProcessView(position, convertView);
        return convertView;
    }

    protected abstract int getLayoutResId();

    protected abstract IHolder getHolder();

    public interface IHolder {
        void onBindView(View convertView);

        void onProcessView(int position, View convertView);
    }
}

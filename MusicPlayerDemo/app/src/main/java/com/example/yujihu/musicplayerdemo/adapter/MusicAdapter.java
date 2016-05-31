package com.example.yujihu.musicplayerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yujihu.musicplayerdemo.R;
import com.example.yujihu.musicplayerdemo.bean.Music;

import java.util.List;

/**
 * Created by yujihu on 2016/5/31.
 */
public class MusicAdapter extends BaseAdapter {
    private List<Music> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public MusicAdapter(Context mContext, List<Music> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        TextView singer;
        TextView song;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);

            holder = new Holder();
            holder.singer = (TextView) convertView.findViewById(R.id.lv_tv_singer_name);
            holder.song = (TextView) convertView.findViewById(R.id.lv_tv_song_name);

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        holder.singer.setText(mData.get(position).getSinger());
        holder.song.setText(mData.get(position).getName());

        return convertView;
    }
}

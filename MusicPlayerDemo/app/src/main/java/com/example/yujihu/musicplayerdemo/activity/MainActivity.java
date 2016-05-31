package com.example.yujihu.musicplayerdemo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.yujihu.musicplayerdemo.R;
import com.example.yujihu.musicplayerdemo.adapter.MusicAdapter;
import com.example.yujihu.musicplayerdemo.bean.Music;
import com.example.yujihu.musicplayerdemo.http.HttpClient;
import com.example.yujihu.musicplayerdemo.http.HttpResponseHandler;
import com.example.yujihu.musicplayerdemo.service.MusicService;
import com.example.yujihu.musicplayerdemo.util.LogUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends Activity {
    public static final String HTTP_HOST = "";

    private ListView mListView;
    private SeekBar mSeekBar;
    private TextView mTextViewCurr;
    private TextView mTextViewTotal;
    private TextView mTextViewMusicNmae;

    private ImageButton mImageButtonNext;
    private ImageButton mImageButtonPrev;
    private ImageButton mImageButtonPlay;

    private Context mContext;

    private MusicReceiver mMusicReceiver;
    private int musicIndex = -1;
    private List<Music> musics;

    public static boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        loadData();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_song_list);
        mSeekBar = (SeekBar) findViewById(R.id.sb_progress);
        mSeekBar.setMax(-999);

        mTextViewCurr = (TextView) findViewById(R.id.tv_curr_time);
        mTextViewMusicNmae = (TextView) findViewById(R.id.tv_song_name);
        mTextViewTotal = (TextView) findViewById(R.id.tv_total_time);

        mImageButtonPlay = (ImageButton) findViewById(R.id.ib_play);
        mImageButtonNext = (ImageButton) findViewById(R.id.ib_next);
        mImageButtonPrev = (ImageButton) findViewById(R.id.ib_pre);

        ImageButtonClickListener listener = new ImageButtonClickListener();
        mImageButtonPlay.setOnClickListener(listener);
        mImageButtonPrev.setOnClickListener(listener);
        mImageButtonNext.setOnClickListener(listener);

        mMusicReceiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter(MusicService.CASE_ACTION_UPDATE);
        registerReceiver(mMusicReceiver, filter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,MusicService.class);
                intent.putExtra(MusicService.KEY_COMMAND,MusicService.CMD_PLAY);
                intent.putExtra(MusicService.KEY_MUSIC_INDEX,position);
                startService(intent);

                isPlaying = true;
                mImageButtonPlay.setBackgroundResource(R.drawable.ic_player_pause_bg);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isPlaying){
            mImageButtonPlay.setBackgroundResource(R.drawable.ic_player_pause_bg);
        }else {
            mImageButtonPlay.setBackgroundResource(R.drawable.ic_player_play_bg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMusicReceiver);
    }

    private class ImageButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MusicService.class);

            switch (v.getId()) {
                case R.id.ib_play:
                    if (isPlaying) {
                        intent.putExtra(MusicService.KEY_COMMAND, MusicService.CMD_PAUSE);
                        isPlaying = false;
                        mImageButtonPlay.setBackgroundResource(R.drawable.ic_player_play_bg);
                    } else {
                        intent.putExtra(MusicService.KEY_COMMAND, MusicService.CMD_RESUME);
                        isPlaying = true;
                        mImageButtonPlay.setBackgroundResource(R.drawable.ic_player_pause_bg);
                    }
                    break;
                case R.id.ib_next:
                    intent.putExtra(MusicService.KEY_COMMAND, MusicService.CMD_NEXT);
                    break;
                case R.id.ib_pre:
                    intent.putExtra(MusicService.KEY_COMMAND, MusicService.CMD_PREV);
                    break;
                default:
                    break;
            }
            startService(intent);
        }
    }

    private void loadData() {
        HttpClient.getSongs(new HttpResponseHandler() {

            @Override
            public void onSuccess(String content) {
                musics = JSONArray.parseArray(content, Music.class);
                MusicAdapter adapter = new MusicAdapter(MainActivity.this, musics);
                mListView.setAdapter(adapter);

                ArrayList<String> musicList = new ArrayList<String>();
                for (Music music : musics) {
                    musicList.add(HTTP_HOST + music.getMp3());
                }

                Intent intent = new Intent(mContext, MusicService.class);
                intent.putExtra(MusicService.KEY_COMMAND, MusicService.CMD_INIT);
                intent.putExtra(MusicService.KEY_MUSIC_LIST, musicList);
                startService(intent);
//                if (musicIndex != -1) {
//                    mTextViewMusicNmae.setText(list.get(musicIndex).getName());
//                }

            }

            @Override
            public void onFailure(Request request, IOException e) {
                LogUtil.log("加载失败");
            }
        });
    }

    private class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra(MusicService.KEY_MUSIC_INDEX, 0);
            int curr = intent.getIntExtra(MusicService.MUSIC_TIME_CURR, 0);
            int total = intent.getIntExtra(MusicService.MUSIC_TIME_TOTAL, 0);

            if (musicIndex != index) {
                musicIndex = index;
                mSeekBar.setMax(total);
                mTextViewTotal.setText(format(total));
                mTextViewMusicNmae.setText(musics.get(musicIndex).getName());

                mImageButtonPlay.setBackgroundResource(R.drawable.ic_player_pause_bg);
            }
            mSeekBar.setProgress(curr);
            mTextViewCurr.setText(format(curr));
        }
    }

    public static String format(int time) {
        time = time / 1000;
        if (time < 60) {
            return "00:" + formatLong("00", time);
        } else {
            return formatLong("00", time / 60) + ":" + formatLong("00", time % 60);
        }
    }

    public static String formatLong(String format, long time) {
        return new DecimalFormat(format).format(time);
    }
}

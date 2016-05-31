package com.example.yujihu.musicplayerdemo.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.example.yujihu.musicplayerdemo.service.MusicService;

import java.io.IOException;
import java.util.List;

/**
 * Created by yujihu on 2016/5/31.
 */
public class MusicUtil {

    private List<String> musics = null;
    private MediaPlayer mPlayer;
    private Context mContext;
    public static int index = 0;
    public boolean isPlaying = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(MusicService.CASE_ACTION_UPDATE);
            intent.putExtra(MusicService.KEY_MUSIC_INDEX, index);
            intent.putExtra(MusicService.MUSIC_TIME_CURR, mPlayer.getCurrentPosition());
            intent.putExtra(MusicService.MUSIC_TIME_TOTAL, mPlayer.getDuration());

            mContext.sendBroadcast(intent);

            handler.sendEmptyMessageDelayed(1, 500);
        }
    };

    public MusicUtil(Context context, List<String> musics) {
        mPlayer = new MediaPlayer();
        this.musics = musics;
        mContext = context;
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    public void play() {
        mPlayer.start();
        isPlaying = true;

        handler.sendEmptyMessage(1);
    }

    public void play(int index) {
        MusicUtil.index = index;
        if (musics != null) {
            String music = musics.get(MusicUtil.index);

            try {
                mPlayer.reset();

                mPlayer.setDataSource(mContext, Uri.parse(music));
                mPlayer.prepare();
                mPlayer.start();

                isPlaying = true;

                handler.sendEmptyMessage(0);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            isPlaying = false;
        }
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            isPlaying = false;
        }
    }

    public void next() {
        if (musics != null) {
            if (index == musics.size() - 1) {
                index = 0;
            } else {
                ++index;
            }
            play(index);
        }
    }

    public void prev() {
        if (musics != null) {
            if (index == 0) {
                index = musics.size() - 1;
            } else {
                --index;
            }
        }
        play(index);
    }
}

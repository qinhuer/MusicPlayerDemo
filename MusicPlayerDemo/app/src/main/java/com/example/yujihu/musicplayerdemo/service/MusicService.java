package com.example.yujihu.musicplayerdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.yujihu.musicplayerdemo.util.MusicUtil;

import java.util.List;

public class MusicService extends Service {
    //指令
    public static final String KEY_COMMAND = "k_command";
    //音乐名称
    public static final String KEY_MUSIC_NAME = "k_music_name";
    //音乐路径
    public static final String KEY_MUSIC_PATH = "k_music_path";
    //播放位置
    public static final String KEY_MUSIC_INDEX = "k_music_index";
    //音乐列表
    public static final String KEY_MUSIC_LIST = "k_music_list";

    //总播放时长
    public static final String MUSIC_TIME_TOTAL = "music_time_total";
    //当前播放进度
    public static final String MUSIC_TIME_CURR = "music_time_curr";

    //播放进度更新广播
    public static final String CASE_ACTION_UPDATE = "com.yujihu.MUSIC_TIME_UPDATE";

    public static final int CMD_INIT = 1000; //初始化
    public static final int CMD_PLAY = 1001; //播放
    public static final int CMD_PAUSE = 1002; //暂停
    public static final int CMD_NEXT = 1003; //下一曲
    public static final int CMD_PREV = 1004; //上一曲
    public static final int CMD_STOP = 1005; //停止
    public static final int CMD_RESUME = 1006; //从暂停状态恢复

    private MusicUtil mMusicUtil;
    private Context mContext;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int command = intent.getIntExtra(KEY_COMMAND,-1);

        switch (command){
            case CMD_INIT:
                List<String> musiclist = intent.getStringArrayListExtra(KEY_MUSIC_LIST);
                mMusicUtil = new MusicUtil(mContext,musiclist);
                break;
            case CMD_PLAY:
                int index = intent.getIntExtra(KEY_MUSIC_INDEX,0);
                mMusicUtil.play(index);
                break;
            case CMD_PAUSE:
                mMusicUtil.pause();
                break;
            case CMD_RESUME:
                mMusicUtil.play();
                break;
            case CMD_NEXT:
                mMusicUtil.next();
                break;
            case CMD_PREV:
                mMusicUtil.prev();
                break;
            case CMD_STOP:
                mMusicUtil.stop();
                break;
            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}

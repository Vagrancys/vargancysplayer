// IMusicPlayerService.aidl
package com.vargancys.vargancysplayer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
        //打开音乐
        void openAudio(int position);

        //播放音乐
        void start();

        //暂停音乐
        void pause();

        //停止音乐
        void stop();

        /**
         * 得到当前的播放进度
         * @return
         */
        int getCurrentPosition();

        /**
         * 得到当前音频的总时长
         * @return
         */
        int getDuration();

        /**
         * 得到艺术家的名称
         * @return
         */
        String getArtist();

        /**
         * 得到歌曲名称
         * @return
         */
        String getName();

        /**
         * 得到歌曲播放的路径
         * @return
         */
        String getAudioPath();

        /**
         * 播放下一个音乐
         */
        void next();

        /**
         * 播放上一个音乐
         */
        void pre();

        /**
         * 设置播放模式
         * @param playmode
         */
        void setPlayMode(int playmode);

        /**
         * 得到播放模式
         * @return
         */
        int getPlayMode();

        /**
        *
        *得到播放状态
        */
        boolean isPlaying();

        void seekTo(int progress);

        int getAudioSessionId();
}











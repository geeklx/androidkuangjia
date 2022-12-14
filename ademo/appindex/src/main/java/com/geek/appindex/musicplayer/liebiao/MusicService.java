package com.geek.appindex.musicplayer.liebiao;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.geek.libutils.app.BaseApp;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    public MusicService() {
    }

    public class MyBinder extends Binder {
        private boolean isPlaying = false;

        public void play(String path) {
            //Log.i("TAG", "MyBinderPlay");
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setAudioAttributes(new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            isPlaying = true;
                        }
                    });

                } else {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        isPlaying = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void pause() {
            //Log.i("TAG", "MyBinderPause");
            if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                mediaPlayer.pause();
                isPlaying = false;
            } else if (mediaPlayer != null && (!mediaPlayer.isPlaying())) {
                mediaPlayer.start();
                isPlaying = true;
            }
        }

        /**
         * ?????????????????????
         */
        private void stop() {
            if (mediaPlayer != null) {
                mediaPlayer.pause();            //???????????????
                mediaPlayer.seekTo(0);    //??????0
                mediaPlayer.stop();             //??????
            }

        }

        /**
         * ??????
         *
         * @param path ?????????????????????
         */
        public void cut(String path) {
            if (mediaPlayer != null) {
                stop();
//                ????????????????????????
                mediaPlayer.reset();
//                ????????????????????????
                try {
                    mediaPlayer.setDataSource(path);
//                    if (path.contains("http") /*|| path.contains(getExternalFilesDir(null).getAbsolutePath())*/) {
//                        mediaPlayer.setDataSource(path);
//                    } else {
//                        AssetManager assetManager = BaseApp.get().getAssets();
//                        AssetFileDescriptor fileDescriptor = assetManager.openFd(path);
//                        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());// ???????????????
//
//                    }
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            isPlaying = true;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.play(path);
            }

        }

        public void onCompletion() {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }


        // ????????????????????????
        public int getCurrTime() {
            return mediaPlayer.getCurrentPosition();
        }

        // ?????????????????????
        public int getTotalTime() {
            return mediaPlayer.getDuration();
        }

        // ????????????????????????
        public void seekToProgress(int progress) {
            mediaPlayer.seekTo(progress);
        }

        // ???????????????????????????
        public boolean getIsPlaying() {
            return isPlaying;
        }

        // ??????mediaPlayer????????????
        public boolean isNull() {
            return mediaPlayer == null ? true : false;
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "IBinder");
        return new MyBinder();
    }
}

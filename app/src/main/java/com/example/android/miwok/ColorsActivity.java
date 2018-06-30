/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;
    AudioManager aManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ){
                mMediaPlayer.stop();
                releaseMediaPlayer();
            } else if( focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        aManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> colorWords = new ArrayList<Word>();

        colorWords.add(new Word("red", "wetetti", R.drawable.color_red, R.raw.color_red));
        colorWords.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        colorWords.add(new Word("brown", "takaaki", R.drawable.color_brown, R.raw.color_brown));
        colorWords.add(new Word("gray", "topoppi", R.drawable.color_gray, R.raw.color_gray));
        colorWords.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        colorWords.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        colorWords.add(new Word("dusty yellow", "topiise", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorWords.add(new Word("mustard yellow", "chiwiite", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdaptor adapter = new WordAdaptor(this, colorWords, R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                releaseMediaPlayer();

                int audioPlayback = aManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                Log.i("Phrases Activity", "AudioPlay: " + audioPlayback);
                if (audioPlayback == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, colorWords.get(position).getAudioID());

                    Log.i("AudioFocus Check", "Audio Focus Granted");

                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            releaseMediaPlayer();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            /*
             * Abandon audiofocus as a result of media player losing focus or finishing.
             */
            aManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}

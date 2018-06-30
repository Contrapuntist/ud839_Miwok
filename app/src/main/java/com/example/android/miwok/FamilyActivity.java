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
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;
    AudioManager aManager;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                Log.i("AudioFocus", "Audio Focus Gained");
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ){
                mMediaPlayer.stop();
                releaseMediaPlayer();
            } else if( focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mMediaPlayer.pause();
            } else if ( focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ){
                mMediaPlayer.pause();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        aManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> familyWords = new ArrayList<Word>();

        familyWords.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        familyWords.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        familyWords.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        familyWords.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        familyWords.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyWords.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyWords.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyWords.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyWords.add(new Word("grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyWords.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));


        WordAdaptor adapter = new WordAdaptor(this, familyWords, R.color.category_family);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                int audioPlayback = aManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (audioPlayback == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, familyWords.get(position).getAudioID());

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
            aManager.abandonAudioFocus(afChangeListener);
        }
    }
}

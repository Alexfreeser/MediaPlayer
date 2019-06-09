package com.example.mediaplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button buttonPlay;
    private View imageViewRedNote;
    private View imageViewGreenNote;
    SeekBar seekBarCrossfade;
    Button buttonSelectSong1;
    Button buttonSelectSong2;

    private int shortAnimationDuration;
    private int max = 10;
    private int min = 2;

    String filePath;
    MediaPlayer mp;

    static ArrayList<String> songsPath;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPlay = findViewById(R.id.buttonPlay);
        imageViewGreenNote = findViewById(R.id.imageViewGreenNote);
        imageViewRedNote = findViewById(R.id.imageViewRedNote);
        buttonSelectSong1 = findViewById(R.id.buttonSelectSong1);
        buttonSelectSong2 = findViewById(R.id.buttonSelectSong2);
        seekBarCrossfade = findViewById(R.id.seekBarCrossfade);
        seekBarCrossfade.setMax(max);
        seekBarCrossfade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < min) {
                    seekBar.setProgress(min);
                }
                shortAnimationDuration = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarCrossfade.setProgress(min);
        imageViewGreenNote.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("path")) {
            filePath = intent.getStringExtra("path");
            songsPath.add(filePath);
        } else {
            Toast.makeText(this, "Songs not found", Toast.LENGTH_SHORT).show();
        }

    }



    public void onClickPlay(View view) {
        index = 0;
        initMediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (index < songsPath.size() - 1) {
                    index++;
                    initMediaPlayer();
                    mp.setOnCompletionListener(this);
                } else if (index == songsPath.size() - 1) {
                    index--;
                    initMediaPlayer();
                    mp.setOnCompletionListener(this);
                }
            }
        });
            crossfade();
    }

    public void initMediaPlayer() {
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(songsPath.get(index));
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }


    private void crossfade() {

        imageViewGreenNote.setAlpha(0f);
        imageViewGreenNote.setVisibility(View.VISIBLE);

        imageViewGreenNote.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);


        imageViewRedNote.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imageViewRedNote.setVisibility(View.GONE);
                    }
                });
    }

    public void onClickSelect(View view) {
        Intent intent = new Intent(MainActivity.this, SongListActivity.class);
        startActivity(intent);
        buttonSelectSong1.setClickable(false);
    }

    public void onClickSelect2(View view) {
        Intent intent = new Intent(MainActivity.this, SongListActivity.class);
        startActivity(intent);
        buttonSelectSong2.setClickable(false);
    }
}

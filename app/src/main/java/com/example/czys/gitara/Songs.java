package com.example.czys.gitara;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leff.midi.MidiFile;

import org.billthefarmer.mididriver.MidiDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Songs extends AppCompatActivity implements MidiDriver.OnMidiStartListener,
        View.OnTouchListener{

    private MidiDriver midiDriver;
    private byte[] event;

    byte[][] notes = {
            {41, 42, 43, 44, 40},
            {46, 47, 48, 49, 45},
            {51, 52, 53, 54, 50},
            {56, 57, 58, 59, 55},
            {60, 61, 62, 63, 59},
            {65, 66, 67, 68, 64}

    };

    ImageButton[][] notesButtons = new ImageButton[6][5];
    ImageView[][] notesButtonsClick = new ImageView[6][5];

    ImageView[][] notesSongOuter = new ImageView[6][4];
    ImageView[][] notesSongInner = new ImageView[6][4];
    ImageView[] notesSongEmpty = new ImageView[6];

    Button soundRadio;
    TextView tempoBPM;
    Button tempoPlus;
    Button tempoMinus;
    Button metronomeRadio;

    ArrayList<TextView> songs = new ArrayList<>();
    int lastSongId = 0;
    int BPM = 0;
    boolean withSound = true;
    boolean withMetronome = false;
    String chosenSong = null;
    public static MediaPlayer playerTemp;
    public static MediaPlayer playerMetronome;
    AnimatorSet animSet1;
    AnimatorSet animSet2;
    AnimatorSet animSet3;
    AnimatorSet arrowAnimatorSet;

    ImageButton informationButton;
    Button okButton;
    boolean isError = false;

    boolean[][] isNotePlaying = new boolean[6][5];

    DrawerLayout drawer;
    boolean isDrawerOpen;

    private void selectInstrument(int instrument) {

        // Construct a program change to select the instrument on channel 1:
        event = new byte[2];
        event[0] = (byte)(0xC0 | 0x00); // 0xC0 = program change, 0x00 = channel 1
        event[1] = (byte)instrument;

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        drawer = findViewById(R.id.drawer_layout);
        findViewById(R.id.nav_view).getBackground().setAlpha(0);

        notesButtons[0][0] = findViewById(R.id.notesButtons00);
        notesButtons[0][1] = findViewById(R.id.notesButtons01);
        notesButtons[0][2] = findViewById(R.id.notesButtons02);
        notesButtons[0][3] = findViewById(R.id.notesButtons03);
        notesButtons[0][4] = findViewById(R.id.notesButtons04);

        notesButtons[1][0] = findViewById(R.id.notesButtons10);
        notesButtons[1][1] = findViewById(R.id.notesButtons11);
        notesButtons[1][2] = findViewById(R.id.notesButtons12);
        notesButtons[1][3] = findViewById(R.id.notesButtons13);
        notesButtons[1][4] = findViewById(R.id.notesButtons14);

        notesButtons[2][0] = findViewById(R.id.notesButtons20);
        notesButtons[2][1] = findViewById(R.id.notesButtons21);
        notesButtons[2][2] = findViewById(R.id.notesButtons22);
        notesButtons[2][3] = findViewById(R.id.notesButtons23);
        notesButtons[2][4] = findViewById(R.id.notesButtons24);

        notesButtons[3][0] = findViewById(R.id.notesButtons30);
        notesButtons[3][1] = findViewById(R.id.notesButtons31);
        notesButtons[3][2] = findViewById(R.id.notesButtons32);
        notesButtons[3][3] = findViewById(R.id.notesButtons33);
        notesButtons[3][4] = findViewById(R.id.notesButtons34);

        notesButtons[4][0] = findViewById(R.id.notesButtons40);
        notesButtons[4][1] = findViewById(R.id.notesButtons41);
        notesButtons[4][2] = findViewById(R.id.notesButtons42);
        notesButtons[4][3] = findViewById(R.id.notesButtons43);
        notesButtons[4][4] = findViewById(R.id.notesButtons44);

        notesButtons[5][0] = findViewById(R.id.notesButtons50);
        notesButtons[5][1] = findViewById(R.id.notesButtons51);
        notesButtons[5][2] = findViewById(R.id.notesButtons52);
        notesButtons[5][3] = findViewById(R.id.notesButtons53);
        notesButtons[5][4] = findViewById(R.id.notesButtons54);

        notesButtonsClick[0][0] = findViewById(R.id.notesButtons00Click);
        notesButtonsClick[0][1] = findViewById(R.id.notesButtons01Click);
        notesButtonsClick[0][2] = findViewById(R.id.notesButtons02Click);
        notesButtonsClick[0][3] = findViewById(R.id.notesButtons03Click);
        notesButtonsClick[0][4] = findViewById(R.id.notesButtons04Click);

        notesButtonsClick[1][0] = findViewById(R.id.notesButtons10Click);
        notesButtonsClick[1][1] = findViewById(R.id.notesButtons11Click);
        notesButtonsClick[1][2] = findViewById(R.id.notesButtons12Click);
        notesButtonsClick[1][3] = findViewById(R.id.notesButtons13Click);
        notesButtonsClick[1][4] = findViewById(R.id.notesButtons14Click);

        notesButtonsClick[2][0] = findViewById(R.id.notesButtons20Click);
        notesButtonsClick[2][1] = findViewById(R.id.notesButtons21Click);
        notesButtonsClick[2][2] = findViewById(R.id.notesButtons22Click);
        notesButtonsClick[2][3] = findViewById(R.id.notesButtons23Click);
        notesButtonsClick[2][4] = findViewById(R.id.notesButtons24Click);

        notesButtonsClick[3][0] = findViewById(R.id.notesButtons30Click);
        notesButtonsClick[3][1] = findViewById(R.id.notesButtons31Click);
        notesButtonsClick[3][2] = findViewById(R.id.notesButtons32Click);
        notesButtonsClick[3][3] = findViewById(R.id.notesButtons33Click);
        notesButtonsClick[3][4] = findViewById(R.id.notesButtons34Click);

        notesButtonsClick[4][0] = findViewById(R.id.notesButtons40Click);
        notesButtonsClick[4][1] = findViewById(R.id.notesButtons41Click);
        notesButtonsClick[4][2] = findViewById(R.id.notesButtons42Click);
        notesButtonsClick[4][3] = findViewById(R.id.notesButtons43Click);
        notesButtonsClick[4][4] = findViewById(R.id.notesButtons44Click);

        notesButtonsClick[5][0] = findViewById(R.id.notesButtons50Click);
        notesButtonsClick[5][1] = findViewById(R.id.notesButtons51Click);
        notesButtonsClick[5][2] = findViewById(R.id.notesButtons52Click);
        notesButtonsClick[5][3] = findViewById(R.id.notesButtons53Click);
        notesButtonsClick[5][4] = findViewById(R.id.notesButtons54Click);

        //notesSong

        notesSongOuter[0][0] = findViewById(R.id.notesSongOuter00);
        notesSongOuter[0][1] = findViewById(R.id.notesSongOuter01);
        notesSongOuter[0][2] = findViewById(R.id.notesSongOuter02);
        notesSongOuter[0][3] = findViewById(R.id.notesSongOuter03);

        notesSongOuter[1][0] = findViewById(R.id.notesSongOuter10);
        notesSongOuter[1][1] = findViewById(R.id.notesSongOuter11);
        notesSongOuter[1][2] = findViewById(R.id.notesSongOuter12);
        notesSongOuter[1][3] = findViewById(R.id.notesSongOuter13);

        notesSongOuter[2][0] = findViewById(R.id.notesSongOuter20);
        notesSongOuter[2][1] = findViewById(R.id.notesSongOuter21);
        notesSongOuter[2][2] = findViewById(R.id.notesSongOuter22);
        notesSongOuter[2][3] = findViewById(R.id.notesSongOuter23);

        notesSongOuter[3][0] = findViewById(R.id.notesSongOuter30);
        notesSongOuter[3][1] = findViewById(R.id.notesSongOuter31);
        notesSongOuter[3][2] = findViewById(R.id.notesSongOuter32);
        notesSongOuter[3][3] = findViewById(R.id.notesSongOuter33);

        notesSongOuter[4][0] = findViewById(R.id.notesSongOuter40);
        notesSongOuter[4][1] = findViewById(R.id.notesSongOuter41);
        notesSongOuter[4][2] = findViewById(R.id.notesSongOuter42);
        notesSongOuter[4][3] = findViewById(R.id.notesSongOuter43);

        notesSongOuter[5][0] = findViewById(R.id.notesSongOuter50);
        notesSongOuter[5][1] = findViewById(R.id.notesSongOuter51);
        notesSongOuter[5][2] = findViewById(R.id.notesSongOuter52);
        notesSongOuter[5][3] = findViewById(R.id.notesSongOuter53);


        notesSongInner[0][0] = findViewById(R.id.notesSongInner00);
        notesSongInner[0][1] = findViewById(R.id.notesSongInner01);
        notesSongInner[0][2] = findViewById(R.id.notesSongInner02);
        notesSongInner[0][3] = findViewById(R.id.notesSongInner03);

        notesSongInner[1][0] = findViewById(R.id.notesSongInner10);
        notesSongInner[1][1] = findViewById(R.id.notesSongInner11);
        notesSongInner[1][2] = findViewById(R.id.notesSongInner12);
        notesSongInner[1][3] = findViewById(R.id.notesSongInner13);

        notesSongInner[2][0] = findViewById(R.id.notesSongInner20);
        notesSongInner[2][1] = findViewById(R.id.notesSongInner21);
        notesSongInner[2][2] = findViewById(R.id.notesSongInner22);
        notesSongInner[2][3] = findViewById(R.id.notesSongInner23);

        notesSongInner[3][0] = findViewById(R.id.notesSongInner30);
        notesSongInner[3][1] = findViewById(R.id.notesSongInner31);
        notesSongInner[3][2] = findViewById(R.id.notesSongInner32);
        notesSongInner[3][3] = findViewById(R.id.notesSongInner33);

        notesSongInner[4][0] = findViewById(R.id.notesSongInner40);
        notesSongInner[4][1] = findViewById(R.id.notesSongInner41);
        notesSongInner[4][2] = findViewById(R.id.notesSongInner42);
        notesSongInner[4][3] = findViewById(R.id.notesSongInner43);

        notesSongInner[5][0] = findViewById(R.id.notesSongInner50);
        notesSongInner[5][1] = findViewById(R.id.notesSongInner51);
        notesSongInner[5][2] = findViewById(R.id.notesSongInner52);
        notesSongInner[5][3] = findViewById(R.id.notesSongInner53);

        notesSongEmpty[0] = findViewById(R.id.notesSongEmpty04);
        notesSongEmpty[1] = findViewById(R.id.notesSongEmpty14);
        notesSongEmpty[2] = findViewById(R.id.notesSongEmpty24);
        notesSongEmpty[3] = findViewById(R.id.notesSongEmpty34);
        notesSongEmpty[4] = findViewById(R.id.notesSongEmpty44);
        notesSongEmpty[5] = findViewById(R.id.notesSongEmpty54);


        //Animations
        notesButtons[0][4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));
        findViewById(R.id.notesButtons04Text).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));

        notesButtons[1][4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));
        findViewById(R.id.notesButtons14Text).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));

        notesButtons[2][4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));
        findViewById(R.id.notesButtons24Text).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));

        notesButtons[3][4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));
        findViewById(R.id.notesButtons34Text).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));

        notesButtons[4][4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));
        findViewById(R.id.notesButtons44Text).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));

        notesButtons[5][4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));
        findViewById(R.id.notesButtons54Text).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidenotebuttonempty));

        findViewById(R.id.returnArrow).startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidereturnarrow));

        //Listeners
        for (int i=0; i<6; i++)
            for (int j=0; j<5; j++)
                notesButtons[i][j].setOnTouchListener(this);

        findViewById(R.id.returnArrow).setOnTouchListener(this);
        findViewById(R.id.Play).setOnTouchListener(this);

        // Instantiate the driver.
        midiDriver = new MidiDriver();
        // Set the listener.
        midiDriver.setOnMidiStartListener(this);

        informationButton = findViewById(R.id.informationButton);
        okButton = findViewById(R.id.okButton);
        soundRadio = findViewById(R.id.SoundRadio);
        tempoBPM = findViewById(R.id.TempoBPM);
        tempoPlus = findViewById(R.id.TempoPlus);
        tempoMinus = findViewById(R.id.TempoMinus);
        metronomeRadio = findViewById(R.id.MetronomeRadio);

        animSet1 = new AnimatorSet();
        animSet2 = new AnimatorSet();
        animSet3 = new AnimatorSet();
        arrowAnimatorSet = new AnimatorSet();

        addSongsToMenu();

        informationButton.setOnTouchListener(this);
        okButton.setOnTouchListener(this);
        soundRadio.setOnTouchListener(this);
        tempoPlus.setOnTouchListener(this);
        tempoMinus.setOnTouchListener(this);
        metronomeRadio.setOnTouchListener(this);

        if(!songs.isEmpty()) {
            chosenSong = songs.get(0).getText().toString() + ".mid";
            BPM = (int) MidiUtils.getBPM(MidiUtils.loadMIDI(chosenSong));

            playerTemp = new MediaPlayer();
            playerMetronome = new MediaPlayer();
        }

        tempoBPM.setText(BPM + " BPM");

        for (TextView tv: songs
             ) {
            tv.setOnTouchListener(this);
        }

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!songs.isEmpty()) {
                    MidiUtils.stopTempMidi();
                    MidiUtils.stopMetronome();
                    animSet1.end();
                    animSet2.end();
                    animSet3.end();
                    arrowAnimatorSet.end();
                }
            }
        };
        // Set the drawer toggle as the DrawerListener
        drawer.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final View decorView = this.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (!isDrawerOpen) openDrawerOnCreate();

        midiDriver.start();
    }

    void openDrawerOnCreate() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START, true);
        isDrawerOpen = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        midiDriver.stop();
    }

    @Override
    public void onMidiStart() {
        Log.d(this.getClass().getName(), "onMidiStart()");
    }

    private void sendPlayNote(byte note) {

        selectInstrument(getIntent().getIntExtra("instrument", 24));

        // Construct a note ON message for the middle C at maximum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x90 | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = note;  // 0x3C = middle C
        event[2] = (byte) 127;  // 0x7F = the maximum velocity (127)

        // Internally this just calls write() and can be considered obsoleted:
        //midiDriver.queueEvent(event);

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    private void sendStopNote(byte note) {

        // Construct a note OFF message for the middle C at minimum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x80 | 0x00);  // 0x80 = note Off, 0x00 = channel 1
        event[1] = note;  // 0x3C = middle C
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    private void playNote(ImageView notesButtonsClick, byte notes, int string, int notePosition) {
        notesButtonsClick.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(notesButtonsClick, View.ALPHA, 1.0f).setDuration(100).start();
        sendPlayNote(notes);
        isNotePlaying[string][notePosition] = true;
    }

    private void stopNote(ImageView notesButtonsClick, byte notes, int string, int notePosition) {
        ObjectAnimator.ofFloat(notesButtonsClick, View.ALPHA, 0.0f).setDuration(150).start();
        sendStopNote(notes);
        isNotePlaying[string][notePosition] = false;
    }

    public boolean onTouch(View v, MotionEvent event) {

        Log.d(this.getClass().getName(), "Motion event: " + event);

        if (v.getId() == R.id.returnArrow){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                findViewById(R.id.returnArrowClick).setVisibility(View.VISIBLE);
                if(!songs.isEmpty()) {
                    MidiUtils.stopTempMidi();
                    MidiUtils.stopMetronome();
                    playerTemp.release();
                    playerMetronome.release();
                }
                finish();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.returnArrowClick).setVisibility(View.INVISIBLE);
            }
        }

        if (!drawer.isDrawerOpen(GravityCompat.START)) {

            //String 0
            switch (v.getId()){
                case R.id.notesButtons00: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][1] && !isNotePlaying[0][2] && !isNotePlaying[0][3] && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][0], notes[0][0], 0, 0);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][0], notes[0][0], 0, 0);
                    break;
                }
                case R.id.notesButtons01: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][2] && !isNotePlaying[0][3] && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][1], notes[0][1], 0, 1);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][1], notes[0][1], 0, 1);
                    break;
                }

                case R.id.notesButtons02: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][3] && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][2], notes[0][2], 0, 2);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][2], notes[0][2], 0, 2);
                    break;
                }

                case R.id.notesButtons03: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][3], notes[0][3], 0, 3);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][3], notes[0][3], 0, 3);
                    break;
                }

                case R.id.notesButtons04: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[0][4], notes[0][4], 0, 4);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][4], notes[0][4], 0, 4);
                    break;
                }

                // String 1
                case R.id.notesButtons10: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][1] && !isNotePlaying[1][2] && !isNotePlaying[1][3] && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][0], notes[1][0], 1, 0);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][0], notes[1][0], 1, 0);
                    break;
                }
                case R.id.notesButtons11: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][2] && !isNotePlaying[1][3] && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][1], notes[1][1], 1, 1);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][1], notes[1][1], 1, 1);
                    break;
                }

                case R.id.notesButtons12: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][3] && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][2], notes[1][2], 1, 2);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][2], notes[1][2], 1, 2);
                    break;
                }

                case R.id.notesButtons13: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][3], notes[1][3], 1, 3);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][3], notes[1][3], 1, 3);
                    break;
                }

                case R.id.notesButtons14: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[1][4], notes[1][4], 1, 4);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][4], notes[1][4], 1, 4);
                    break;
                }

                // String 2
                case R.id.notesButtons20: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][1] && !isNotePlaying[2][2] && !isNotePlaying[2][3] && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][0], notes[2][0], 2, 0);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][0], notes[2][0], 2, 0);
                    break;
                }
                case R.id.notesButtons21: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][2] && !isNotePlaying[2][3] && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][1], notes[2][1], 2, 1);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][1], notes[2][1], 2, 1);
                    break;
                }

                case R.id.notesButtons22: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][3] && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][2], notes[2][2], 2, 2);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][2], notes[2][2], 2, 2);
                    break;
                }

                case R.id.notesButtons23: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][3], notes[2][3], 2, 3);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][3], notes[2][3], 2, 3);
                    break;
                }

                case R.id.notesButtons24: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[2][4], notes[2][4], 2, 4);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][4], notes[2][4], 2, 4);
                    break;
                }

                // String 3
                case R.id.notesButtons30: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][1] && !isNotePlaying[3][2] && !isNotePlaying[3][3] && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][0], notes[3][0], 3, 0);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][0], notes[3][0], 3, 0);
                    break;
                }
                case R.id.notesButtons31: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][2] && !isNotePlaying[3][3] && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][1], notes[3][1], 3, 1);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][1], notes[3][1], 3, 1);
                    break;
                }

                case R.id.notesButtons32: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][3] && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][2], notes[3][2], 3, 2);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][2], notes[3][2], 3, 2);
                    break;
                }

                case R.id.notesButtons33: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][3], notes[3][3], 3, 3);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][3], notes[3][3], 3, 3);
                    break;
                }

                case R.id.notesButtons34: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[3][4], notes[3][4], 3, 4);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][4], notes[3][4], 3, 4);
                    break;
                }

                // String 4
                case R.id.notesButtons40: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][1] && !isNotePlaying[4][2] && !isNotePlaying[4][3] && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][0], notes[4][0], 4, 0);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][0], notes[4][0], 4, 0);
                    break;
                }
                case R.id.notesButtons41: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][2] && !isNotePlaying[4][3] && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][1], notes[4][1], 4, 1);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][1], notes[4][1], 4, 1);
                    break;
                }

                case R.id.notesButtons42: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][3] && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][2], notes[4][2], 4, 2);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][2], notes[4][2], 4, 2);
                    break;
                }

                case R.id.notesButtons43: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][3], notes[4][3], 4, 3);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][3], notes[4][3], 4, 3);
                    break;
                }

                case R.id.notesButtons44: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[4][4], notes[4][4], 4, 4);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][4], notes[4][4], 4, 4);
                    break;
                }

                // String 5
                case R.id.notesButtons50: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][1] && !isNotePlaying[5][2] && !isNotePlaying[5][3] && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][0], notes[5][0], 5, 0);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][0], notes[5][0], 5, 0);
                    break;
                }
                case R.id.notesButtons51: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][2] && !isNotePlaying[5][3] && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][1], notes[5][1], 5, 1);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][1], notes[5][1], 5, 1);
                    break;
                }

                case R.id.notesButtons52: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][3] && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][2], notes[5][2], 5, 2);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][2], notes[5][2], 5, 2);
                    break;
                }

                case R.id.notesButtons53: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][3], notes[5][3], 5, 3);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][3], notes[5][3], 5, 3);
                    break;
                }

                case R.id.notesButtons54: {
                    if (!isError && event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[5][4], notes[5][4], 5, 4);
                    else if (!isError && event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][4], notes[5][4], 5, 4);
                    break;
                }

                case R.id.okButton: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(findViewById(R.id.information), View.ALPHA, 1.0f, 0.0f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(findViewById(R.id.informationBackground), View.ALPHA, 1.0f, 0.0f);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(okButton, View.ALPHA, 1.0f, 0.0f);
                        anim1.setDuration(300);
                        anim2.setDuration(300);
                        anim3.setDuration(300);
                        anim3.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                okButton.setVisibility(View.GONE);
                            }
                        });
                        anim1.start();
                        anim2.start();
                        anim3.start();
                        isError = false;
                    }
                    break;
                }
            }
        }
        else {
            switch (v.getId()) {
                case R.id.informationButton: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        findViewById(R.id.information).setVisibility(View.VISIBLE);
                        findViewById(R.id.informationBackground).setVisibility(View.VISIBLE);
                        okButton.setVisibility(View.VISIBLE);

                        TextView information = findViewById(R.id.information);
                        information.setText(getResources().getText(R.string.songsImport));

                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(findViewById(R.id.information), View.ALPHA, 0.0f, 1.0f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(findViewById(R.id.informationBackground), View.ALPHA, 0.0f, 1.0f);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(okButton, View.ALPHA, 0.0f, 1.0f);
                        anim1.setDuration(300);
                        anim2.setDuration(300);
                        anim3.setDuration(300);
                        anim1.start();
                        anim2.start();
                        anim3.start();
                    }
                    break;
                }

                case R.id.Play: {
                    //if (event.getAction() == MotionEvent.ACTION_DOWN && !songs.isEmpty())

                    if (event.getAction() == MotionEvent.ACTION_DOWN && !songs.isEmpty()) {
                        drawer.closeDrawer(GravityCompat.START);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                isError = false;
                                String error = MidiUtils.prepareTempMidi(chosenSong, BPM);
                                if(error != null) {
                                    isError = true;
                                    findViewById(R.id.information).setVisibility(View.VISIBLE);
                                    findViewById(R.id.informationBackground).setVisibility(View.VISIBLE);
                                    okButton.setVisibility(View.VISIBLE);

                                    TextView information = findViewById(R.id.information);
                                    if(error.equals("incompatibleNotes"))
                                        information.setText(getResources().getText(R.string.incompatibleNotes));
                                    else if(error.equals("resolutionis0"))
                                        information.setText(getResources().getText(R.string.resolutionis0));

                                    ObjectAnimator anim1 = ObjectAnimator.ofFloat(findViewById(R.id.information), View.ALPHA, 0.0f, 1.0f);
                                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(findViewById(R.id.informationBackground), View.ALPHA, 0.0f, 1.0f);
                                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(okButton, View.ALPHA, 0.0f, 1.0f);
                                    anim1.setDuration(300);
                                    anim2.setDuration(300);
                                    anim3.setDuration(300);
                                    anim1.start();
                                    anim2.start();
                                    anim3.start();
                                    return;
                                }

                                showSong();
                                if(withMetronome) {
                                    MidiUtils.prepareMetronome();
                                    MidiUtils.playMetronome();
                                }
                                if(withSound){
                                    MidiUtils.playTempMidi();
                                }
                            }
                        }, 500);
                    }
                    break;
                }

                case R.id.SoundRadio: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(!withSound)
                            soundRadio.setBackground(getResources().getDrawable(R.drawable.metronomeradioon));
                        else
                            soundRadio.setBackground(getResources().getDrawable(R.drawable.metronomeradiooff));
                        withSound = !withSound;
                    }
                    break;
                }

                case R.id.MetronomeRadio: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(!withMetronome)
                            metronomeRadio.setBackground(getResources().getDrawable(R.drawable.metronomeradioon));
                        else
                            metronomeRadio.setBackground(getResources().getDrawable(R.drawable.metronomeradiooff));
                        withMetronome = !withMetronome;
                    }
                    break;
                }

                case R.id.TempoPlus: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !songs.isEmpty() && BPM <= 495) {
                        BPM = BPM + 5;
                        tempoBPM.setText(BPM + " BPM");

                        animSet1 = new AnimatorSet();
                        animSet2 = new AnimatorSet();
                        animSet3 = new AnimatorSet();
                        arrowAnimatorSet = new AnimatorSet();
                    }
                    break;
                }

                case R.id.TempoMinus: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !songs.isEmpty() && BPM >= 6) {
                        BPM = BPM - 5;
                        tempoBPM.setText(BPM + " BPM");

                        animSet1 = new AnimatorSet();
                        animSet2 = new AnimatorSet();
                        animSet3 = new AnimatorSet();
                        arrowAnimatorSet = new AnimatorSet();
                    }
                    break;
                }
            }

            for (TextView tv: songs
                    ) {
                if(v.getId() == tv.getId()){
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        songs.get(lastSongId).setBackground(getResources().getDrawable(R.drawable.textlines));

                        chosenSong = tv.getText().toString() + ".mid";
                        BPM = (int) MidiUtils.getBPM(MidiUtils.loadMIDI(chosenSong));
                        tempoBPM.setText(BPM + " BPM");

                        tv.setBackground(getResources().getDrawable(R.drawable.chordsmenuclick));
                        lastSongId = tv.getId();

                        animSet1 = new AnimatorSet();
                        animSet2 = new AnimatorSet();
                        animSet3 = new AnimatorSet();
                        arrowAnimatorSet = new AnimatorSet();
                    }
                    break;
                }
            }
        }
        return false;
    }

    private void addSongsToMenu(){
        ArrayList<String> preparedSongs = new ArrayList<>();
        preparedSongs.add("a_gamacdur");
        preparedSongs.add("b_silentnight");
        preparedSongs.add("c_deye");
        preparedSongs.add("d_happyb");
        preparedSongs.add("e_mala");
        preparedSongs.add("f_godfather");
        preparedSongs.add("g_guan");
        preparedSongs.add("h_ode");
        preparedSongs.add("i_imagine");
        preparedSongs.add("j_obladi");
        preparedSongs.add("k_oque");
        preparedSongs.add("l_amazinggrace");
        preparedSongs.add("m_falling");
        preparedSongs.add("n_house");

        String[] preparedSongsNames = getResources().getStringArray(R.array.songsNames);

        InputStream inputStream;
        OutputStream outputStream = null;

        for (int i=0; i<preparedSongsNames.length; i++) {
            inputStream = getResources().openRawResource(
                    getResources().getIdentifier(preparedSongs.get(i),
                            "raw", getPackageName()));
            try {
                outputStream =
                        new FileOutputStream(new File(MidiUtils.sdPath + preparedSongsNames[i] + ".mid"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            int read;
            byte[] bytes = new byte[1024];

            try {
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File dir = new File(MidiUtils.sdPath);
        LinearLayout ll = findViewById(R.id.songsScrollViewLinearLayout);
        int count = 0;

        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if(files[i].getName().compareTo("_temp.mid") == 0 || files[i].getName().compareTo("_metronome.mid") == 0)
                    continue;
                File file = files[i];
                TextView tv = new TextView(this);
                tv.setId(count++);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)convertDpToPixel(60, this)));
                tv.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                tv.setText(removeExtension(file.getName()));
                tv.setTextColor(getResources().getColor(R.color.white80));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                tv.setClickable(true);
                tv.setGravity(Gravity.CENTER);
                if(tv.getId() == 0)
                    tv.setBackground(getResources().getDrawable(R.drawable.chordsmenuclick));
                else
                    tv.setBackground(getResources().getDrawable(R.drawable.textlines));

                songs.add(tv);
                ll.addView(tv);
            }
        }

    }

    public static String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path up to the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;

        return filename.substring(0, extensionIndex);
    }

    private float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private float ticksToMs(long ticks, int BPM, int resolution){
        float ms = (ticks * (float) 60000 / (BPM * resolution));
        return ms;
    }

    private void showSong() {
        MidiFile mf = MidiUtils.loadMIDI();

        ArrayList<Long> ticksOn = MidiUtils.getNoteOnTicks(mf);
        ArrayList<Byte> notesValues = MidiUtils.getNotes(mf);

        HashMap<Byte, Long> previousNotes = new HashMap<>();

        LinkedList<Long> notesForChords = new LinkedList<>();

        long arrowStart, arrowEnd;
        ImageView arrowDown = findViewById(R.id.arrowDown);
        ImageView arrowUp = findViewById(R.id.arrowUp);

        int resolution = MidiUtils.getResolution(mf);
        int BPM = (int) MidiUtils.getBPM(mf);

        mainLoop:
        for (int i=0; i<ticksOn.size(); i++){

            long animationDuration = (resolution * 2) - 100;

            for (Byte previousNoteValue: previousNotes.keySet()
                 ) {
                if(previousNoteValue == notesValues.get(i) && (ticksOn.get(i) - previousNotes.get(previousNoteValue)) < animationDuration){
                    Log.d("ticks", "" + (ticksOn.get(i) - previousNotes.get(previousNoteValue)));
                    animationDuration = (ticksOn.get(i) - previousNotes.get(previousNoteValue)) - 100;

                    if(animationDuration < 0)
                        animationDuration = 0;
                }
            }
            Log.d("animation Duration", "" + animationDuration);

            previousNotes.put(notesValues.get(i), ticksOn.get(i));


            //for arrow
            if(i==0){
                //notesForChords.addLast(ticksOn.get(0));
            }
            else if(ticksOn.get(i) - ticksOn.get(i-1) > 62 || ticksOn.get(i) - ticksOn.get(i-1) == 0 || i == ticksOn.size() - 1) {
                if(notesForChords.size() >= 3){
                    arrowStart = notesForChords.getFirst();
                    arrowEnd = notesForChords.getLast();

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    if(notesValues.get(i-1) > notesValues.get(i-2)) {
                        arrowDown.setVisibility(View.VISIBLE);
                        arrowDown.setAlpha(0.0f);
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(arrowDown, View.TRANSLATION_X, -((metrics.widthPixels) / 6) * notesForChords.size(), 1.0f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(arrowDown, View.ALPHA, 0.0f, 1.0f);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(arrowDown, View.ALPHA, 1.0f, 0.0f);

                        anim1.setStartDelay((long) ticksToMs(arrowStart - (animationDuration / 6), BPM, resolution));
                        anim2.setStartDelay((long) ticksToMs(arrowStart - (animationDuration / 6), BPM, resolution));
                        anim3.setStartDelay((long) ticksToMs(arrowEnd + 100, BPM, resolution));

                        anim1.setDuration((long) ticksToMs((arrowEnd + (animationDuration / 6) + 100) - arrowStart, BPM, resolution));
                        anim2.setDuration((long) ticksToMs(((arrowEnd + (animationDuration / 6)  + 100) - arrowStart) / 2, BPM, resolution));
                        anim3.setDuration((long) ticksToMs(100, BPM, resolution));

                        arrowAnimatorSet.play(anim1);
                        arrowAnimatorSet.play(anim2);
                        arrowAnimatorSet.play(anim3);
                    }
                    else {
                        arrowUp.setVisibility(View.VISIBLE);
                        arrowUp.setAlpha(0.0f);
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(arrowUp, View.TRANSLATION_X, metrics.widthPixels, metrics.widthPixels - (((metrics.widthPixels) / 6) * notesForChords.size()));
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(arrowUp, View.ALPHA, 0.0f, 1.0f);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(arrowUp, View.ALPHA, 1.0f, 0.0f);

                        anim1.setStartDelay((long) ticksToMs(arrowStart - (animationDuration / 6), BPM, resolution));
                        anim2.setStartDelay((long) ticksToMs(arrowStart - (animationDuration / 6), BPM, resolution));
                        anim3.setStartDelay((long) ticksToMs(arrowEnd + 100, BPM, resolution));

                        anim1.setDuration((long) ticksToMs((arrowEnd + (animationDuration / 6) + 100) - arrowStart, BPM, resolution));
                        anim2.setDuration((long) ticksToMs(((arrowEnd + (animationDuration / 6) + 100) - arrowStart) / 2, BPM, resolution));
                        anim3.setDuration((long) ticksToMs(100, BPM, resolution));

                        arrowAnimatorSet.play(anim1);
                        arrowAnimatorSet.play(anim2);
                        arrowAnimatorSet.play(anim3);
                    }
                }
                notesForChords.clear();
            }

            notesForChords.addLast(ticksOn.get(i));

            for (int j = 0 ; j < 6; j++) {
                for (int k = 0; k < 5; k++) {
                    if (notes[j][k] == notesValues.get(i)) {
                        if(j==3 && k==3)
                            break;
                        if(k==4){
                            notesSongEmpty[j].setVisibility(View.VISIBLE);
                            notesSongEmpty[j].setAlpha(0.0f);

                            ObjectAnimator animK1 = ObjectAnimator.ofFloat(notesSongEmpty[j], View.SCALE_X, 0.0f, 1.0f);
                            animK1.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                            animK1.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                            Log.d("animK1", "" + animK1.getStartDelay());

                            ObjectAnimator animK2 = ObjectAnimator.ofFloat(notesSongEmpty[j], View.SCALE_Y, 0.0f, 1.0f);
                            animK2.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                            animK2.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                            Log.d("animK2", "" + animK2.getStartDelay());

                            ObjectAnimator animK3 = ObjectAnimator.ofFloat(notesSongEmpty[j], View.ALPHA, 0.0f, 1.0f);
                            animK3.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                            animK3.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                            Log.d("animK3", "" + animK3.getStartDelay());

                            ObjectAnimator animK4 = ObjectAnimator.ofFloat(notesSongEmpty[j], View.ALPHA, 1.0f, 0.0f);
                            animK4.setDuration((long)ticksToMs(50, BPM, resolution));
                            animK4.setStartDelay((long)ticksToMs(ticksOn.get(i) + 50, BPM, resolution));
                            Log.d("animK4", "" + animK4.getStartDelay());

                            animSet1.play(animK1);
                            animSet1.play(animK2);
                            animSet1.play(animK3);
                            animSet1.play(animK4);

                            continue mainLoop;
                        }


                        //Outer
                        notesSongOuter[j][k].setVisibility(View.VISIBLE);
                        notesSongOuter[j][k].setAlpha(0.0f);

                        ObjectAnimator animOuter1 = ObjectAnimator.ofFloat(notesSongOuter[j][k], View.ALPHA, 0.0f, 1.0f);
                        animOuter1.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                        animOuter1.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                        Log.d("outer animOuter1", "" + j + " " + k + " " + animOuter1.getStartDelay() + " duration: " + animOuter1.getDuration());

                        ObjectAnimator animOuter2 = ObjectAnimator.ofFloat(notesSongOuter[j][k], View.ALPHA, 1.0f, 0.0f);
                        animOuter2.setDuration((long)ticksToMs(50, BPM, resolution));
                        animOuter2.setStartDelay((long)ticksToMs(ticksOn.get(i) + 50, BPM, resolution));
                        Log.d("outer animOuter2", "" + j + " " + k + " " + animOuter2.getStartDelay() + " duration: " + animOuter2.getDuration());

                        animSet2.play(animOuter1);
                        animSet2.play(animOuter2);


                        //Inner
                        notesSongInner[j][k].setVisibility(View.VISIBLE);
                        notesSongInner[j][k].setAlpha(0.0f);

                        ObjectAnimator animInner1 = ObjectAnimator.ofFloat(notesSongInner[j][k], View.SCALE_X, 0.0f, 1.0f);
                        animInner1.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                        animInner1.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                        Log.d("inner animInner1", "" + j + " " + k + " " + animInner1.getStartDelay() + " duration: " + animInner1.getDuration());

                        ObjectAnimator animInner2 = ObjectAnimator.ofFloat(notesSongInner[j][k], View.SCALE_Y, 0.0f, 1.0f);
                        animInner2.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                        animInner2.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                        Log.d("inner animInner1", "" + j + " " + k + " " + animInner2.getStartDelay() + " duration: " + animInner2.getDuration());

                        ObjectAnimator animInner3 = ObjectAnimator.ofFloat(notesSongInner[j][k], View.ALPHA, 0.0f, 1.0f);
                        animInner3.setDuration((long)ticksToMs(animationDuration, BPM, resolution));
                        animInner3.setStartDelay((long)ticksToMs(ticksOn.get(i) - animationDuration, BPM, resolution));
                        Log.d("inner animInner1", "" + j + " " + k + " " + animInner3.getStartDelay() + " duration: " + animInner3.getDuration());

                        ObjectAnimator animInner4 = ObjectAnimator.ofFloat(notesSongInner[j][k], View.ALPHA, 1.0f, 0.0f);
                        animInner4.setDuration((long)ticksToMs(50, BPM, resolution));
                        animInner4.setStartDelay((long)ticksToMs(ticksOn.get(i) + 50, BPM, resolution));
                        Log.d("inner animInner3", "" + j + " " + k + " " + animInner4.getStartDelay() + " duration: " + animInner4.getDuration());

                        animSet3.play(animInner1);
                        animSet3.play(animInner2);
                        animSet3.play(animInner3);
                        animSet3.play(animInner4);

                        continue mainLoop;
                    }
                }
            }
        }
        animSet1.start();
        animSet2.start();
        animSet3.start();
        arrowAnimatorSet.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

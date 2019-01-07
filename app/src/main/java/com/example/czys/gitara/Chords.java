package com.example.czys.gitara;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.billthefarmer.mididriver.MidiDriver;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class Chords extends AppCompatActivity implements MidiDriver.OnMidiStartListener,
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

    TextView[] keysTexts = new TextView[12];

    //not all values get filled
    TextView[][] chordsTexts = new TextView[12][11];
    TextView[][] chordsTextsFlats = new TextView[11][11];

    ImageButton[][] notesButtons = new ImageButton[6][5];
    TextView[][] notesButtonsClick = new TextView[6][5];

    ArrayList<TextView> lastNoteButtonClickForInvisible = new ArrayList<>();
    ArrayList<TextView> lastKeyForDrawableChange = new ArrayList<>();
    ArrayList<TextView> lastChordForDrawableChange = new ArrayList<>();
    ArrayList<TextView> lastChordForGone = new ArrayList<>();

    boolean[][] isNotePlaying = new boolean[6][5];

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
        setContentView(R.layout.chords);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        keysTexts[0] = findViewById(R.id.keys_Ab);
        keysTexts[1] = findViewById(R.id.keys_A);
        keysTexts[2] = findViewById(R.id.keys_Bb);
        if(Locale.getDefault().getLanguage() != "en")
            findViewById(R.id.keys_Bb_flat).setVisibility(View.GONE);
        keysTexts[3] = findViewById(R.id.keys_B);
        keysTexts[4] = findViewById(R.id.keys_C);
        keysTexts[5] = findViewById(R.id.keys_Db);
        keysTexts[6] = findViewById(R.id.keys_D);
        keysTexts[7] = findViewById(R.id.keys_Eb);
        keysTexts[8] = findViewById(R.id.keys_E);
        keysTexts[9] = findViewById(R.id.keys_F);
        keysTexts[10] = findViewById(R.id.keys_Fsharp);
        keysTexts[11] = findViewById(R.id.keys_G);

        chordsTexts[0][0] = findViewById(R.id.chord_Ab6);
        chordsTexts[0][1] = findViewById(R.id.chord_Ab7);
        chordsTexts[0][2] = findViewById(R.id.chord_Ab9);
        chordsTexts[0][3] = findViewById(R.id.chord_GsharpM6);
        chordsTexts[0][4] = findViewById(R.id.chord_GsharpM7);
        chordsTexts[0][5] = findViewById(R.id.chord_Abmaj7);
        chordsTexts[0][6] = findViewById(R.id.chord_GsharpDim);
        chordsTexts[0][7] = findViewById(R.id.chord_AbPlus);
        chordsTexts[0][8] = findViewById(R.id.chord_AbSus);
        chordsTexts[0][9] = null;
        chordsTexts[0][10] = null;

        chordsTextsFlats[0][0] = findViewById(R.id.chord_Ab6_flat);
        chordsTextsFlats[0][1] = findViewById(R.id.chord_Ab7_flat);
        chordsTextsFlats[0][2] = findViewById(R.id.chord_Ab9_flat);
        chordsTextsFlats[0][3] = findViewById(R.id.chord_Abmaj7_flat);
        chordsTextsFlats[0][4] = findViewById(R.id.chord_AbPlus_flat);
        chordsTextsFlats[0][5] = findViewById(R.id.chord_AbSus_flat);
        chordsTextsFlats[0][6] = null;
        chordsTextsFlats[0][7] = null;
        chordsTextsFlats[0][8] = null;
        chordsTextsFlats[0][9] = null;
        chordsTextsFlats[0][10] = null;

        chordsTexts[1][0] = findViewById(R.id.chord_A);
        chordsTexts[1][1] = findViewById(R.id.chord_Am);
        chordsTexts[1][2] = findViewById(R.id.chord_A6);
        chordsTexts[1][3] = findViewById(R.id.chord_A7);
        chordsTexts[1][4] = findViewById(R.id.chord_A9);
        chordsTexts[1][5] = findViewById(R.id.chord_Am6);
        chordsTexts[1][6] = findViewById(R.id.chord_Am7);
        chordsTexts[1][7] = findViewById(R.id.chord_Amaj7);
        chordsTexts[1][8] = findViewById(R.id.chord_Adim);
        chordsTexts[1][9] = findViewById(R.id.chord_Aplus);
        chordsTexts[1][10] = findViewById(R.id.chord_Asus);

        chordsTexts[2][0] = findViewById(R.id.chord_Bb);
        chordsTexts[2][1] = findViewById(R.id.chord_BbM);
        chordsTexts[2][2] = findViewById(R.id.chord_Bb6);
        chordsTexts[2][3] = findViewById(R.id.chord_BbM6);
        chordsTexts[2][4] = findViewById(R.id.chord_BbM7);
        chordsTexts[2][5] = findViewById(R.id.chord_Bbmaj7);
        chordsTexts[2][6] = findViewById(R.id.chord_BbDim);
        chordsTexts[2][7] = findViewById(R.id.chord_BbPlus);
        chordsTexts[2][8] = findViewById(R.id.chord_BbSus);
        chordsTexts[2][9] = null;
        chordsTexts[2][10] = null;

        if(Locale.getDefault().getLanguage() == "en") {
            chordsTextsFlats[2][0] = findViewById(R.id.chord_Bb_flat);
            chordsTextsFlats[2][1] = findViewById(R.id.chord_BbM_flat);
            chordsTextsFlats[2][2] = findViewById(R.id.chord_Bb6_flat);
            chordsTextsFlats[2][3] = findViewById(R.id.chord_BbM6_flat);
            chordsTextsFlats[2][4] = findViewById(R.id.chord_BbM7_flat);
            chordsTextsFlats[2][5] = findViewById(R.id.chord_Bbmaj7_flat);
            chordsTextsFlats[2][6] = findViewById(R.id.chord_BbDim_flat);
            chordsTextsFlats[2][7] = findViewById(R.id.chord_BbPlus_flat);
            chordsTextsFlats[2][8] = findViewById(R.id.chord_BbSus_flat);
            chordsTextsFlats[2][9] = null;
            chordsTextsFlats[2][10] = null;
        }

        chordsTexts[3][0] = findViewById(R.id.chord_B);
        chordsTexts[3][1] = findViewById(R.id.chord_Bm);
        chordsTexts[3][2] = findViewById(R.id.chord_B6);
        chordsTexts[3][3] = findViewById(R.id.chord_B7);
        chordsTexts[3][4] = findViewById(R.id.chord_B9);
        chordsTexts[3][5] = findViewById(R.id.chord_Bm6);
        chordsTexts[3][6] = findViewById(R.id.chord_Bmaj7);
        chordsTexts[3][7] = findViewById(R.id.chord_Bdim);
        chordsTexts[3][8] = null;
        chordsTexts[3][9] = null;
        chordsTexts[3][10] = null;


        chordsTexts[4][0] = findViewById(R.id.chord_C);
        chordsTexts[4][1] = findViewById(R.id.chord_C6);
        chordsTexts[4][2] = findViewById(R.id.chord_C7);
        chordsTexts[4][3] = findViewById(R.id.chord_C9);
        chordsTexts[4][4] = findViewById(R.id.chord_Cm6);
        chordsTexts[4][5] = findViewById(R.id.chord_Cm7);
        chordsTexts[4][6] = findViewById(R.id.chord_Cmaj7);
        chordsTexts[4][7] = findViewById(R.id.chord_Cdim);
        chordsTexts[4][8] = findViewById(R.id.chord_Cplus);
        chordsTexts[4][9] = findViewById(R.id.chord_Csus);
        chordsTexts[4][10] = null;

        chordsTexts[5][0] = findViewById(R.id.chord_Db);
        chordsTexts[5][1] = findViewById(R.id.chord_CsharpM);
        chordsTexts[5][2] = findViewById(R.id.chord_Db6);
        chordsTexts[5][3] = findViewById(R.id.chord_Db7);
        chordsTexts[5][4] = findViewById(R.id.chord_Db9);
        chordsTexts[5][5] = findViewById(R.id.chord_CsharpM6);
        chordsTexts[5][6] = findViewById(R.id.chord_CsharpM7);
        chordsTexts[5][7] = findViewById(R.id.chord_Dbmaj7);
        chordsTexts[5][8] = findViewById(R.id.chord_CsharpDim);
        chordsTexts[5][9] = findViewById(R.id.chord_DbPlus);
        chordsTexts[5][10] = findViewById(R.id.chord_DbSus);

        chordsTextsFlats[5][0] = findViewById(R.id.chord_Db_flat);
        chordsTextsFlats[5][1] = findViewById(R.id.chord_Db6_flat);
        chordsTextsFlats[5][2] = findViewById(R.id.chord_Db7_flat);
        chordsTextsFlats[5][3] = findViewById(R.id.chord_Db9_flat);
        chordsTextsFlats[5][4] = findViewById(R.id.chord_Dbmaj7_flat);
        chordsTextsFlats[5][5] = findViewById(R.id.chord_DbPlus_flat);
        chordsTextsFlats[5][6] = findViewById(R.id.chord_DbSus_flat);
        chordsTextsFlats[5][7] = null;
        chordsTextsFlats[5][8] = null;
        chordsTextsFlats[5][9] = null;
        chordsTextsFlats[5][10] = null;

        chordsTexts[6][0] = findViewById(R.id.chord_D);
        chordsTexts[6][1] = findViewById(R.id.chord_Dm);
        chordsTexts[6][2] = findViewById(R.id.chord_D6);
        chordsTexts[6][3] = findViewById(R.id.chord_D7);
        chordsTexts[6][4] = findViewById(R.id.chord_D9);
        chordsTexts[6][5] = findViewById(R.id.chord_Dm6);
        chordsTexts[6][6] = findViewById(R.id.chord_Dm7);
        chordsTexts[6][7] = findViewById(R.id.chord_Dmaj7);
        chordsTexts[6][8] = findViewById(R.id.chord_Ddim);
        chordsTexts[6][9] = findViewById(R.id.chord_Dplus);
        chordsTexts[6][10] = findViewById(R.id.chord_Dsus);

        chordsTexts[7][0] = findViewById(R.id.chord_EbM);
        chordsTexts[7][1] = findViewById(R.id.chord_Eb6);
        chordsTexts[7][2] = findViewById(R.id.chord_Eb7);
        chordsTexts[7][3] = findViewById(R.id.chord_Eb9);
        chordsTexts[7][4] = findViewById(R.id.chord_EbM6);
        chordsTexts[7][5] = findViewById(R.id.chord_EbM7);
        chordsTexts[7][6] = findViewById(R.id.chord_Ebmaj7);
        chordsTexts[7][7] = findViewById(R.id.chord_EbDim);
        chordsTexts[7][8] = findViewById(R.id.chord_EbPlus);
        chordsTexts[7][9] = findViewById(R.id.chord_EbSus);
        chordsTexts[7][10] = null;

        chordsTextsFlats[7][0] = findViewById(R.id.chord_EbM_flat);
        chordsTextsFlats[7][1] = findViewById(R.id.chord_Eb6_flat);
        chordsTextsFlats[7][2] = findViewById(R.id.chord_Eb7_flat);
        chordsTextsFlats[7][3] = findViewById(R.id.chord_Eb9_flat);
        chordsTextsFlats[7][4] = findViewById(R.id.chord_EbM6_flat);
        chordsTextsFlats[7][5] = findViewById(R.id.chord_EbM7_flat);
        chordsTextsFlats[7][6] = findViewById(R.id.chord_Ebmaj7_flat);
        chordsTextsFlats[7][7] = findViewById(R.id.chord_EbDim_flat);
        chordsTextsFlats[7][8] = findViewById(R.id.chord_EbPlus_flat);
        chordsTextsFlats[7][9] = findViewById(R.id.chord_EbSus_flat);
        chordsTextsFlats[7][10] = null;

        chordsTexts[8][0] = findViewById(R.id.chord_E);
        chordsTexts[8][1] = findViewById(R.id.chord_Em);
        chordsTexts[8][2] = findViewById(R.id.chord_E6);
        chordsTexts[8][3] = findViewById(R.id.chord_E7);
        chordsTexts[8][4] = findViewById(R.id.chord_E9);
        chordsTexts[8][5] = findViewById(R.id.chord_Em6);
        chordsTexts[8][6] = findViewById(R.id.chord_Em7);
        chordsTexts[8][7] = findViewById(R.id.chord_Emaj7);
        chordsTexts[8][8] = findViewById(R.id.chord_Edim);
        chordsTexts[8][9] = findViewById(R.id.chord_Eplus);
        chordsTexts[8][10] = findViewById(R.id.chord_Esus);

        chordsTexts[9][0] = findViewById(R.id.chord_F);
        chordsTexts[9][1] = findViewById(R.id.chord_Fm);
        chordsTexts[9][2] = findViewById(R.id.chord_F6);
        chordsTexts[9][3] = findViewById(R.id.chord_F7);
        chordsTexts[9][4] = findViewById(R.id.chord_F9);
        chordsTexts[9][5] = findViewById(R.id.chord_Fm6);
        chordsTexts[9][6] = findViewById(R.id.chord_Fm7);
        chordsTexts[9][7] = findViewById(R.id.chord_Fmaj7);
        chordsTexts[9][8] = findViewById(R.id.chord_Fdim);
        chordsTexts[9][9] = findViewById(R.id.chord_Fplus);
        chordsTexts[9][10] = findViewById(R.id.chord_Fsus);

        chordsTexts[10][0] = findViewById(R.id.chord_Fsharp);
        chordsTexts[10][1] = findViewById(R.id.chord_FsharpM);
        chordsTexts[10][2] = findViewById(R.id.chord_Gb6);
        chordsTexts[10][3] = findViewById(R.id.chord_Fsharp7);
        chordsTexts[10][4] = findViewById(R.id.chord_FsharpM6);
        chordsTexts[10][5] = findViewById(R.id.chord_FsharpM7);
        chordsTexts[10][6] = findViewById(R.id.chord_Gbmaj7);
        chordsTexts[10][7] = findViewById(R.id.chord_FsharpDim);
        chordsTexts[10][8] = findViewById(R.id.chord_GbPlus);
        chordsTexts[10][9] = findViewById(R.id.chord_GbSus);
        chordsTexts[10][10] = null;

        chordsTextsFlats[10][0] = findViewById(R.id.chord_Gb6_flat);
        chordsTextsFlats[10][1] = findViewById(R.id.chord_Gbmaj7_flat);
        chordsTextsFlats[10][2] = findViewById(R.id.chord_GbPlus_flat);
        chordsTextsFlats[10][3] = findViewById(R.id.chord_GbSus_flat);
        chordsTextsFlats[10][4] = null;
        chordsTextsFlats[10][5] = null;
        chordsTextsFlats[10][6] = null;
        chordsTextsFlats[10][7] = null;
        chordsTextsFlats[10][8] = null;
        chordsTextsFlats[10][9] = null;
        chordsTextsFlats[10][10] = null;

        chordsTexts[11][0] = findViewById(R.id.chord_G);
        chordsTexts[11][1] = findViewById(R.id.chord_G6);
        chordsTexts[11][2] = findViewById(R.id.chord_G7);
        chordsTexts[11][3] = findViewById(R.id.chord_G9);
        chordsTexts[11][4] = findViewById(R.id.chord_Gm6);
        chordsTexts[11][5] = findViewById(R.id.chord_Gdim);
        chordsTexts[11][6] = findViewById(R.id.chord_Gplus);
        chordsTexts[11][7] = findViewById(R.id.chord_Gsus);

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

        // Instantiate the driver.
        midiDriver = new MidiDriver();
        // Set the listener.
        midiDriver.setOnMidiStartListener(this);
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

    private void playNote(TextView notesButtonsClick, byte notes, int string, int notePosition) {
        notesButtonsClick.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(notesButtonsClick, View.ALPHA, 1.0f).setDuration(100).start();
        notesButtonsClick.setText(null);
        sendPlayNote(notes);
        isNotePlaying[string][notePosition] = true;
    }

    private void stopNote(TextView notesButtonsClick, byte notes, int string, int notePosition) {
        ObjectAnimator.ofFloat(notesButtonsClick, View.ALPHA, 0.0f).setDuration(150).start();
        sendStopNote(notes);
        isNotePlaying[string][notePosition] = false;
    }

    public boolean onTouch(View v, MotionEvent event) {

        Log.d(this.getClass().getName(), "Motion event: " + event);

        if (v.getId() == R.id.returnArrow){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                findViewById(R.id.returnArrowClick).setVisibility(View.VISIBLE);
                finish();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.returnArrowClick).setVisibility(View.INVISIBLE);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            //String 0
            switch (v.getId()){
                case R.id.notesButtons00: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][1] && !isNotePlaying[0][2] && !isNotePlaying[0][3] && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][0], notes[0][0], 0, 0);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][0], notes[0][0], 0, 0);
                    break;
                }
                case R.id.notesButtons01: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][2] && !isNotePlaying[0][3] && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][1], notes[0][1], 0, 1);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][1], notes[0][1], 0, 1);
                    break;
                }

                case R.id.notesButtons02: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][3] && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][2], notes[0][2], 0, 2);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][2], notes[0][2], 0, 2);
                    break;
                }

                case R.id.notesButtons03: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[0][4])
                        playNote(notesButtonsClick[0][3], notes[0][3], 0, 3);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][3], notes[0][3], 0, 3);
                    break;
                }

                case R.id.notesButtons04: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[0][4], notes[0][4], 0, 4);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[0][4], notes[0][4], 0, 4);
                    break;
                }

                // String 1
                case R.id.notesButtons10: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][1] && !isNotePlaying[1][2] && !isNotePlaying[1][3] && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][0], notes[1][0], 1, 0);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][0], notes[1][0], 1, 0);
                    break;
                }
                case R.id.notesButtons11: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][2] && !isNotePlaying[1][3] && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][1], notes[1][1], 1, 1);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][1], notes[1][1], 1, 1);
                    break;
                }

                case R.id.notesButtons12: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][3] && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][2], notes[1][2], 1, 2);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][2], notes[1][2], 1, 2);
                    break;
                }

                case R.id.notesButtons13: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[1][4])
                        playNote(notesButtonsClick[1][3], notes[1][3], 1, 3);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][3], notes[1][3], 1, 3);
                    break;
                }

                case R.id.notesButtons14: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[1][4], notes[1][4], 1, 4);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[1][4], notes[1][4], 1, 4);
                    break;
                }

                // String 2
                case R.id.notesButtons20: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][1] && !isNotePlaying[2][2] && !isNotePlaying[2][3] && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][0], notes[2][0], 2, 0);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][0], notes[2][0], 2, 0);
                    break;
                }
                case R.id.notesButtons21: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][2] && !isNotePlaying[2][3] && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][1], notes[2][1], 2, 1);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][1], notes[2][1], 2, 1);
                    break;
                }

                case R.id.notesButtons22: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][3] && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][2], notes[2][2], 2, 2);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][2], notes[2][2], 2, 2);
                    break;
                }

                case R.id.notesButtons23: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[2][4])
                        playNote(notesButtonsClick[2][3], notes[2][3], 2, 3);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][3], notes[2][3], 2, 3);
                    break;
                }

                case R.id.notesButtons24: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[2][4], notes[2][4], 2, 4);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[2][4], notes[2][4], 2, 4);
                    break;
                }

                // String 3
                case R.id.notesButtons30: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][1] && !isNotePlaying[3][2] && !isNotePlaying[3][3] && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][0], notes[3][0], 3, 0);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][0], notes[3][0], 3, 0);
                    break;
                }
                case R.id.notesButtons31: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][2] && !isNotePlaying[3][3] && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][1], notes[3][1], 3, 1);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][1], notes[3][1], 3, 1);
                    break;
                }

                case R.id.notesButtons32: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][3] && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][2], notes[3][2], 3, 2);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][2], notes[3][2], 3, 2);
                    break;
                }

                case R.id.notesButtons33: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[3][4])
                        playNote(notesButtonsClick[3][3], notes[3][3], 3, 3);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][3], notes[3][3], 3, 3);
                    break;
                }

                case R.id.notesButtons34: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[3][4], notes[3][4], 3, 4);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[3][4], notes[3][4], 3, 4);
                    break;
                }

                // String 4
                case R.id.notesButtons40: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][1] && !isNotePlaying[4][2] && !isNotePlaying[4][3] && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][0], notes[4][0], 4, 0);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][0], notes[4][0], 4, 0);
                    break;
                }
                case R.id.notesButtons41: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][2] && !isNotePlaying[4][3] && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][1], notes[4][1], 4, 1);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][1], notes[4][1], 4, 1);
                    break;
                }

                case R.id.notesButtons42: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][3] && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][2], notes[4][2], 4, 2);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][2], notes[4][2], 4, 2);
                    break;
                }

                case R.id.notesButtons43: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[4][4])
                        playNote(notesButtonsClick[4][3], notes[4][3], 4, 3);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][3], notes[4][3], 4, 3);
                    break;
                }

                case R.id.notesButtons44: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[4][4], notes[4][4], 4, 4);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[4][4], notes[4][4], 4, 4);
                    break;
                }

                // String 5
                case R.id.notesButtons50: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][1] && !isNotePlaying[5][2] && !isNotePlaying[5][3] && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][0], notes[5][0], 5, 0);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][0], notes[5][0], 5, 0);
                    break;
                }
                case R.id.notesButtons51: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][2] && !isNotePlaying[5][3] && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][1], notes[5][1], 5, 1);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][1], notes[5][1], 5, 1);
                    break;
                }

                case R.id.notesButtons52: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][3] && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][2], notes[5][2], 5, 2);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][2], notes[5][2], 5, 2);
                    break;
                }

                case R.id.notesButtons53: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && !isNotePlaying[5][4])
                        playNote(notesButtonsClick[5][3], notes[5][3], 5, 3);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][3], notes[5][3], 5, 3);
                    break;
                }

                case R.id.notesButtons54: {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        playNote(notesButtonsClick[5][4], notes[5][4], 5, 4);
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                        stopNote(notesButtonsClick[5][4], notes[5][4], 5, 4);
                    break;
                }
            }
        }
        return false;
    }

    private void playChord(TextView chordsTexts, byte[][] chord, String[] fingers){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        for (TextView e: lastNoteButtonClickForInvisible
                ) {
            e.setVisibility(View.INVISIBLE);
        }
        for (TextView e: lastChordForDrawableChange
                ) {
            e.setBackgroundResource(R.drawable.textlines);
        }

        lastNoteButtonClickForInvisible.clear();
        lastChordForDrawableChange.clear();

        chordsTexts.setBackgroundResource(R.drawable.chordsmenuclick);

        for (int i=0; i<chord.length; i++){
            sendPlayNote(notes[chord[i][0]][chord[i][1]]);
            notesButtonsClick[chord[i][0]][chord[i][1]].setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(notesButtonsClick[chord[i][0]][chord[i][1]], View.ALPHA, 1.0f).setDuration(200).start();

            notesButtonsClick[chord[i][0]][chord[i][1]].setText(fingers[i]);
            notesButtonsClick[chord[i][0]][chord[i][1]].setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            notesButtonsClick[chord[i][0]][chord[i][1]].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
            notesButtonsClick[chord[i][0]][chord[i][1]].setTextColor(getResources().getColor(R.color.black20));
            notesButtonsClick[chord[i][0]][chord[i][1]].setGravity(Gravity.CENTER);

            lastNoteButtonClickForInvisible.add(notesButtonsClick[chord[i][0]][chord[i][1]]);
            lastChordForDrawableChange.add(chordsTexts);
        }
    }

    private void changeKey(TextView keysTexts, TextView[][] chordsTexts, TextView[][] chordsTextsFlats, int key, int numberOfChords, int numberOfFlats) {
        for (TextView e: lastKeyForDrawableChange
                ) {
            e.setBackgroundResource(R.drawable.textlines);
        }
        for (TextView e: lastChordForGone
                ) {
            e.setVisibility(View.GONE);
        }

        lastKeyForDrawableChange.clear();
        lastChordForGone.clear();

        findViewById(R.id.chordsScrollView).scrollTo(0, 0);
        keysTexts.setBackgroundResource(R.drawable.chordsmenuclick);
        for (int i=0; i<numberOfChords; i++)
            chordsTexts[key][i].setVisibility(View.VISIBLE);
        for (int i=0; i<numberOfFlats; i++)
            chordsTextsFlats[key][i].setVisibility(View.VISIBLE);

        lastKeyForDrawableChange.add(keysTexts);
        for (int i=0; i<numberOfChords; i++)
            lastChordForGone.add(chordsTexts[key][i]);
        for (int i=0; i<numberOfFlats; i++)
            lastChordForGone.add(chordsTextsFlats[key][i]);
    }

    //Overloaded for Keys without flats
    private void changeKey(TextView keysTexts, TextView chordsTexts[][], int key, int numberOfChords) {
        for (TextView e: lastKeyForDrawableChange
                ) {
            e.setBackgroundResource(R.drawable.textlines);
        }
        for (TextView e: lastChordForGone
                ) {
            e.setVisibility(View.GONE);
        }

        lastKeyForDrawableChange.clear();
        lastChordForGone.clear();

        findViewById(R.id.chordsScrollView).scrollTo(0, 0);
        keysTexts.setBackgroundResource(R.drawable.chordsmenuclick);
        for (int i=0; i<numberOfChords; i++)
            chordsTexts[key][i].setVisibility(View.VISIBLE);

        lastKeyForDrawableChange.add(keysTexts);
        for (int i=0; i<numberOfChords; i++)
            lastChordForGone.add(chordsTexts[key][i]);
    }

    public void onClick(View v) {

        Log.v(this.getClass().getName(), "View: " + v);

        switch (v.getId()) {
            //Keys
            case R.id.keys_Ab: {
                changeKey(keysTexts[0], chordsTexts, chordsTextsFlats, 0, 9, 6);
                break;
            }

            case R.id.keys_A: {
                changeKey(keysTexts[1], chordsTexts, 1, 11);
                break;
            }

            case R.id.keys_Bb: {
                if(Locale.getDefault().getLanguage() == "en")
                    changeKey(keysTexts[2], chordsTexts, chordsTextsFlats, 2, 9, 9);
                else
                    changeKey(keysTexts[2], chordsTexts, 2, 9);
                break;
            }

            case R.id.keys_B: {
                changeKey(keysTexts[3], chordsTexts, 3, 8);
                break;
            }

            case R.id.keys_C: {
                changeKey(keysTexts[4], chordsTexts, 4, 10);
                break;
            }

            case R.id.keys_Db: {
                changeKey(keysTexts[5], chordsTexts, chordsTextsFlats, 5, 11, 7);
                break;
            }

            case R.id.keys_D: {
                changeKey(keysTexts[6], chordsTexts, 6, 11);
                break;
            }

            case R.id.keys_Eb: {
                changeKey(keysTexts[7], chordsTexts, chordsTextsFlats, 7, 10, 10);
                break;
            }

            case R.id.keys_E: {
                changeKey(keysTexts[8], chordsTexts, 8, 11);
                break;
            }

            case R.id.keys_F: {
                changeKey(keysTexts[9], chordsTexts, 9, 11);
                break;
            }

            case R.id.keys_Fsharp: {
                changeKey(keysTexts[10], chordsTexts, chordsTextsFlats, 10, 10, 4);
                break;
            }

            case R.id.keys_G: {
                changeKey(keysTexts[11], chordsTexts, 11, 8);
                break;
            }

            //Chords
            //Key_Ab
            case R.id.chord_Ab6: {
                byte[][] chord = {
                        {0, 3},
                        {1, 2},
                        {2, 0},
                        {3, 0},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"5", "4", "2", "2", "2", "2"};

                playChord(chordsTexts[0][0], chord, fingers);
                break;
            }

            case R.id.chord_Ab7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 0},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "2", "2", "3"};

                playChord(chordsTexts[0][1], chord, fingers);
                break;
            }

            case R.id.chord_Ab9: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "4", "2", "3"};

                playChord(chordsTexts[0][2], chord, fingers);
                break;
            }

            case R.id.chord_GsharpM6: {
                byte[][] chord = {
                        {3, 3},
                        {4, 3},
                        {5, 3}
                };

                String[] fingers = {"4", "3", "2"};

                playChord(chordsTexts[0][3], chord, fingers);
                break;
            }

            case R.id.chord_GsharpM7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 0},
                        {4, 4},
                        {5, 1}
                };

                String[] fingers = {"3", "2", "", "4"};

                playChord(chordsTexts[0][4], chord, fingers);
                break;
            }

            case R.id.chord_Abmaj7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 0},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"2", "2", "2", "4"};

                playChord(chordsTexts[0][5], chord, fingers);
                break;
            }

            case R.id.chord_GsharpDim: {
                byte[][] chord = {
                        {2, 4},
                        {3, 0},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"", "2", "", "3"};

                playChord(chordsTexts[0][6], chord, fingers);
                break;
            }

            case R.id.chord_AbPlus: {
                byte[][] chord = {
                        {2, 1},
                        {3, 0},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "3", "2", ""};

                playChord(chordsTexts[0][7], chord, fingers);
                break;
            }

            case R.id.chord_AbSus: {
                byte[][] chord = {
                        {2, 0},
                        {3, 0},
                        {4, 1},
                        {5, 3}
                };

                String[] fingers = {"2", "2", "3", "5"};

                playChord(chordsTexts[0][8], chord, fingers);
                break;
            }


            //Key_A
            case R.id.chord_A: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 1},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", ""};

                playChord(chordsTexts[1][0], chord, fingers);
                break;
            }

            case R.id.chord_Am: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", ""};

                playChord(chordsTexts[1][1], chord, fingers);
                break;
            }

            case R.id.chord_A6: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"", "2", "2", "2", "2"};

                playChord(chordsTexts[1][2], chord, fingers);
                break;
            }

            case R.id.chord_A7: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"", "2", "2", "2", "4"};

                playChord(chordsTexts[1][3], chord, fingers);
                break;
            }

            case R.id.chord_A9: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 3},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"", "2", "4", "2", "3"};

                playChord(chordsTexts[1][4], chord, fingers);
                break;
            }

            case R.id.chord_Am6: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"", "4", "3", "2", "5"};

                playChord(chordsTexts[1][5], chord, fingers);
                break;
            }

            case R.id.chord_Am7: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"", "4", "3", "2", "5"};

                playChord(chordsTexts[1][6], chord, fingers);
                break;
            }

            case R.id.chord_Amaj7: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 0},
                        {4, 1},
                        {5, 4}
                };

                String[] fingers = {"", "3", "2", "4", ""};

                playChord(chordsTexts[1][7], chord, fingers);
                break;
            }

            case R.id.chord_Adim: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "4", "2", "3"};

                playChord(chordsTexts[1][8], chord, fingers);
                break;
            }

            case R.id.chord_Aplus: {
                byte[][] chord = {
                        {1, 4},
                        {2, 2},
                        {3, 1},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"", "5", "4", "3", "2"};

                playChord(chordsTexts[1][9], chord, fingers);
                break;
            }

            case R.id.chord_Asus: {
                byte[][] chord = {
                        {1, 4},
                        {2, 1},
                        {3, 1},
                        {4, 2},
                        {5, 4}
                };

                String[] fingers = {"", "3", "2", "4", ""};

                playChord(chordsTexts[1][10], chord, fingers);
                break;
            }

            //Key_Bb
            case R.id.chord_Bb: {
                byte[][] chord = {
                        {1, 0},
                        {2, 2},
                        {3, 2},
                        {4, 2},
                        {5, 0}
                };

                String[] fingers = {"2", "5", "4", "3", "2"};

                playChord(chordsTexts[2][0], chord, fingers);
                break;
            }

            case R.id.chord_BbM: {
                byte[][] chord = {
                        {1, 0},
                        {2, 2},
                        {3, 2},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"2", "5", "4", "3", "2"};

                playChord(chordsTexts[2][1], chord, fingers);
                break;
            }

            case R.id.chord_Bb6: {
                byte[][] chord = {
                        {0, 0},
                        {1, 0},
                        {3, 2},
                        {4, 2},
                        {5, 2}
                };

                String[] fingers = {"2", "2", "5", "4", "3"};

                playChord(chordsTexts[2][2], chord, fingers);
                break;
            }

            case R.id.chord_BbM6: {
                byte[][] chord = {
                        {2, 2},
                        {3, 2},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"4", "3", "2", "5"};

                playChord(chordsTexts[2][3], chord, fingers);
                break;
            }

            case R.id.chord_BbM7: {
                byte[][] chord = {
                        {2, 2},
                        {3, 2},
                        {4, 1},
                        {5, 3}
                };

                String[] fingers = {"4", "3", "2", "5"};

                playChord(chordsTexts[2][4], chord, fingers);
                break;
            }

            case R.id.chord_Bbmaj7: {
                byte[][] chord = {
                        {1, 0},
                        {2, 2},
                        {3, 1},
                        {4, 2},
                };

                String[] fingers = {"2", "5", "3", "4"};

                playChord(chordsTexts[2][5], chord, fingers);
                break;
            }

            case R.id.chord_BbDim: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"2", "4", "2", "3"};

                playChord(chordsTexts[2][6], chord, fingers);
                break;
            }

            case R.id.chord_BbPlus: {
                byte[][] chord = {
                        {2, 4},
                        {3, 2},
                        {4, 2},
                        {5, 1}
                };

                String[] fingers = {"", "4", "3", "2"};

                playChord(chordsTexts[2][7], chord, fingers);
                break;
            }

            case R.id.chord_BbSus: {
                byte[][] chord = {
                        {2, 2},
                        {3, 2},
                        {4, 3},
                        {5, 0}
                };

                String[] fingers = {"4", "3", "5", "2"};

                playChord(chordsTexts[2][8], chord, fingers);
                break;
            }

            //Key_B
            case R.id.chord_B: {
                byte[][] chord = {
                        {1, 1},
                        {2, 3},
                        {3, 3},
                        {4, 3},
                        {5, 1}
                };

                String[] fingers = {"2", "5", "4", "3", "2"};

                playChord(chordsTexts[3][0], chord, fingers);
                break;
            }

            case R.id.chord_Bm: {
                byte[][] chord = {
                        {1, 1},
                        {2, 3},
                        {3, 3},
                        {4, 2},
                        {5, 1}
                };

                String[] fingers = {"2", "5", "4", "3", "2"};

                playChord(chordsTexts[3][1], chord, fingers);
                break;
            }

            case R.id.chord_B6: {
                byte[][] chord = {
                        {1, 1},
                        {2, 1},
                        {3, 3},
                        {4, 3},
                        {5, 3}
                };

                String[] fingers = {"2", "2", "5", "4", "3"};

                playChord(chordsTexts[3][2], chord, fingers);
                break;
            }

            case R.id.chord_B7: {
                byte[][] chord = {
                        {1, 1},
                        {2, 0},
                        {3, 1},
                        {4, 4},
                        {5, 1}
                };

                String[] fingers = {"3", "2", "4", "", "5"};

                playChord(chordsTexts[3][3], chord, fingers);
                break;
            }

            case R.id.chord_B9: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[3][4], chord, fingers);
                break;
            }

            case R.id.chord_Bm6: {
                byte[][] chord = {
                        {2, 3},
                        {3, 3},
                        {4, 2},
                        {5, 3}
                };

                String[] fingers = {"4", "3", "2", "5"};

                playChord(chordsTexts[3][5], chord, fingers);
                break;
            }

            case R.id.chord_Bmaj7: {
                byte[][] chord = {
                        {1, 1},
                        {2, 3},
                        {3, 2},
                        {4, 3}
                };

                String[] fingers = {"2", "4", "3", "5"};

                playChord(chordsTexts[3][6], chord, fingers);
                break;
            }

            case R.id.chord_Bdim: {
                byte[][] chord = {
                        {2, 4},
                        {3, 0},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"", "2", "", "3"};

                playChord(chordsTexts[3][7], chord, fingers);
                break;
            }

            //Key_C
            case R.id.chord_C: {
                byte[][] chord = {
                        {0, 2},
                        {1, 2},
                        {2, 1},
                        {3, 4},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "5", "3", "", "2", ""};

                playChord(chordsTexts[4][0], chord, fingers);
                break;
            }

            case R.id.chord_C6: {
                byte[][] chord = {
                        {2, 1},
                        {3, 1},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"4", "3", "2", "5"};

                playChord(chordsTexts[4][1], chord, fingers);
                break;
            }

            case R.id.chord_C7: {
                byte[][] chord = {
                        {1, 2},
                        {2, 1},
                        {3, 2},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "3", "5", "2", ""};

                playChord(chordsTexts[4][2], chord, fingers);
                break;
            }

            case R.id.chord_C9: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 2},
                        {5, 2}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[4][3], chord, fingers);
                break;
            }

            case R.id.chord_Cm6: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[4][4], chord, fingers);
                break;
            }

            case R.id.chord_Cm7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[4][5], chord, fingers);
                break;
            }

            case R.id.chord_Cmaj7: {
                byte[][] chord = {
                        {1, 2},
                        {2, 1},
                        {3, 4},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"3", "2", "", "", ""};

                playChord(chordsTexts[4][6], chord, fingers);
                break;
            }

            case R.id.chord_Cdim: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "4", "2", "3"};

                playChord(chordsTexts[4][7], chord, fingers);
                break;
            }

            case R.id.chord_Cplus: {
                byte[][] chord = {
                        {2, 1},
                        {3, 0},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "3", "2", ""};

                playChord(chordsTexts[4][8], chord, fingers);
                break;
            }

            case R.id.chord_Csus: {
                byte[][] chord = {
                        {2, 2},
                        {3, 4},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"3", "", "2", "5"};

                playChord(chordsTexts[4][9], chord, fingers);
                break;
            }

            //Key_Db
            case R.id.chord_Db: {
                byte[][] chord = {
                        {2, 2},
                        {3, 0},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"4", "2", "3", "2"};

                playChord(chordsTexts[5][0], chord, fingers);
                break;
            }

            case R.id.chord_CsharpM: {
                byte[][] chord = {
                        {2, 1},
                        {3, 0},
                        {4, 1},
                        {5, 4}
                };

                String[] fingers = {"3", "2", "4", ""};

                playChord(chordsTexts[5][1], chord, fingers);
                break;
            }

            case R.id.chord_Db6: {
                byte[][] chord = {
                        {2, 2},
                        {3, 2},
                        {4, 1},
                        {5, 3}
                };

                String[] fingers = {"4", "3", "2", "5"};

                playChord(chordsTexts[5][2], chord, fingers);
                break;
            }

            case R.id.chord_Db7: {
                byte[][] chord = {
                        {2, 2},
                        {3, 3},
                        {4, 1},
                        {5, 3}
                };

                String[] fingers = {"3", "4", "2", "5"};

                playChord(chordsTexts[5][3], chord, fingers);
                break;
            }

            case R.id.chord_Db9: {
                byte[][] chord = {
                        {2, 2},
                        {3, 3},
                        {4, 3},
                        {5, 3}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[5][4], chord, fingers);
                break;
            }

            case R.id.chord_CsharpM6: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 1},
                        {5, 3}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[5][5], chord, fingers);
                break;
            }

            case R.id.chord_CsharpM7: {
                byte[][] chord = {
                        {2, 1},
                        {3, 3},
                        {4, 1},
                        {5, 3}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[5][6], chord, fingers);
                break;
            }

            case R.id.chord_Dbmaj7: {
                byte[][] chord = {
                        {1, 3},
                        {2, 2},
                        {3, 0},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"5", "4", "2", "2", "2"};

                playChord(chordsTexts[5][7], chord, fingers);
                break;
            }

            case R.id.chord_CsharpDim: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[5][8], chord, fingers);
                break;
            }

            case R.id.chord_DbPlus: {
                byte[][] chord = {
                        {2, 2},
                        {3, 1},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"5", "4", "3", "2"};

                playChord(chordsTexts[5][9], chord, fingers);
                break;
            }

            case R.id.chord_DbSus: {
                byte[][] chord = {
                        {2, 2},
                        {3, 2},
                        {4, 3},
                        {5, 0}
                };

                String[] fingers = {"3", "4", "5", "2"};

                playChord(chordsTexts[5][10], chord, fingers);
                break;
            }

            //Key_D
            case R.id.chord_D: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 2},
                        {5, 1}
                };

                String[] fingers = {"", "2", "4", "3"};

                playChord(chordsTexts[6][0], chord, fingers);
                break;
            }

            case R.id.chord_Dm: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 2},
                        {5, 0}
                };

                String[] fingers = {"", "3", "4", "2"};

                playChord(chordsTexts[6][1], chord, fingers);
                break;
            }

            case R.id.chord_D6: {
                byte[][] chord = {
                        {1, 4},
                        {2, 4},
                        {3, 1},
                        {4, 4},
                        {5, 1}
                };

                String[] fingers = {"", "", "2", "", "3"};

                playChord(chordsTexts[6][2], chord, fingers);
                break;
            }

            case R.id.chord_D7: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"", "3", "2", "4"};

                playChord(chordsTexts[6][3], chord, fingers);
                break;
            }

            case R.id.chord_D9: {
                byte[][] chord = {
                        {0, 1},
                        {1, 4},
                        {2, 4},
                        {3, 1},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "", "", "3", "2", ""};

                playChord(chordsTexts[6][4], chord, fingers);
                break;
            }

            case R.id.chord_Dm6: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"", "3", "", "2"};

                playChord(chordsTexts[6][5], chord, fingers);
                break;
            }

            case R.id.chord_Dm7: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"", "4", "3", "2"};

                playChord(chordsTexts[6][6], chord, fingers);
                break;
            }

            case R.id.chord_Dmaj7: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"", "4", "3", "2"};

                playChord(chordsTexts[6][7], chord, fingers);
                break;
            }

            case R.id.chord_Ddim: {
                byte[][] chord = {
                        {2, 4},
                        {3, 0},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"", "2", "", "3"};

                playChord(chordsTexts[6][8], chord, fingers);
                break;
            }

            case R.id.chord_Dplus: {
                byte[][] chord = {
                        {2, 4},
                        {3, 2},
                        {4, 2},
                        {5, 1}
                };

                String[] fingers = {"", "4", "3", "2"};

                playChord(chordsTexts[6][9], chord, fingers);
                break;
            }

            case R.id.chord_Dsus: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 2},
                        {5, 2}
                };

                String[] fingers = {"", "2", "3", "4"};

                playChord(chordsTexts[6][10], chord, fingers);
                break;
            }

            //Key_Eb
            case R.id.chord_EbM: {
                byte[][] chord = {
                        {2, 3},
                        {3, 2},
                        {4, 3},
                        {5, 1}
                };

                String[] fingers = {"4", "3", "5", "2"};

                playChord(chordsTexts[7][0], chord, fingers);
                break;
            }

            case R.id.chord_Eb6: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[7][1], chord, fingers);
                break;
            }

            case R.id.chord_Eb7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"2", "4", "3", "5"};

                playChord(chordsTexts[7][2], chord, fingers);
                break;
            }

            case R.id.chord_Eb9: {
                byte[][] chord = {
                        {0, 0},
                        {1, 0},
                        {2, 0},
                        {3, 2},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"2", "2", "2", "4", "3", "2"};

                playChord(chordsTexts[7][3], chord, fingers);
                break;
            }

            case R.id.chord_EbM6: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "4", "2", "3"};

                playChord(chordsTexts[7][4], chord, fingers);
                break;
            }

            case R.id.chord_EbM7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[7][5], chord, fingers);
                break;
            }

            case R.id.chord_Ebmaj7: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 2},
                        {5, 2}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[7][6], chord, fingers);
                break;
            }

            case R.id.chord_EbDim: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[7][7], chord, fingers);
                break;
            }

            case R.id.chord_EbPlus: {
                byte[][] chord = {
                        {2, 0},
                        {3, 4},
                        {4, 4},
                        {5, 2}
                };

                String[] fingers = {"2", "", "", "4"};

                playChord(chordsTexts[7][8], chord, fingers);
                break;
            }

            case R.id.chord_EbSus: {
                byte[][] chord = {
                        {2, 0},
                        {3, 2},
                        {4, 3},
                        {5, 3}
                };

                String[] fingers = {"2", "3", "4", "5"};

                playChord(chordsTexts[7][9], chord, fingers);
                break;
            }

            //Key_E
            case R.id.chord_E: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 1},
                        {3, 0},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", "", ""};

                playChord(chordsTexts[8][0], chord, fingers);
                break;
            }

            case R.id.chord_Em: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 1},
                        {3, 4},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"", "3", "2", "", "", ""};

                playChord(chordsTexts[8][1], chord, fingers);
                break;
            }

            case R.id.chord_E6: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 1},
                        {3, 0},
                        {4, 1},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", "5", ""};

                playChord(chordsTexts[8][2], chord, fingers);
                break;
            }

            case R.id.chord_E7: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 1},
                        {3, 0},
                        {4, 2},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", "5", ""};

                playChord(chordsTexts[8][3], chord, fingers);
                break;
            }

            case R.id.chord_E9: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 4},
                        {3, 0},
                        {4, 4},
                        {5, 1}
                };

                String[] fingers = {"", "3", "", "2", "", "4"};

                playChord(chordsTexts[8][4], chord, fingers);
                break;
            }

            case R.id.chord_Em6: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 1},
                        {3, 4},
                        {4, 1},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "", "2", ""};

                playChord(chordsTexts[8][5], chord, fingers);
                break;
            }

            case R.id.chord_Em7: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 4},
                        {3, 4},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"", "2", "", "", "", ""};

                playChord(chordsTexts[8][6], chord, fingers);
                break;
            }

            case R.id.chord_Emaj7: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 0},
                        {3, 0},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", "", ""};

                playChord(chordsTexts[8][7], chord, fingers);
                break;
            }

            case R.id.chord_Edim: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[8][8], chord, fingers);
                break;
            }

            case R.id.chord_Eplus: {
                byte[][] chord = {
                        {2, 1},
                        {3, 0},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "3", "2", ""};

                playChord(chordsTexts[8][9], chord, fingers);
                break;
            }

            case R.id.chord_Esus: {
                byte[][] chord = {
                        {0, 4},
                        {1, 1},
                        {2, 1},
                        {3, 1},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"", "4", "3", "2", "", ""};

                playChord(chordsTexts[8][10], chord, fingers);
                break;
            }

            //Key_F
            case R.id.chord_F: {
                byte[][] chord = {
                        {0, 0},
                        {1, 2},
                        {2, 2},
                        {3, 1},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"2", "5", "4", "3", "2", "2"};

                playChord(chordsTexts[9][0], chord, fingers);
                break;
            }

            case R.id.chord_Fm: {
                byte[][] chord = {
                        {0, 0},
                        {1, 2},
                        {2, 2},
                        {3, 0},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"2", "4", "3", "2", "2", "2"};

                playChord(chordsTexts[9][1], chord, fingers);
                break;
            }

            case R.id.chord_F6: {
                byte[][] chord = {
                        {2, 4},
                        {3, 1},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"", "4", "3", "2"};

                playChord(chordsTexts[9][2], chord, fingers);
                break;
            }

            case R.id.chord_F7: {
                byte[][] chord = {
                        {0, 0},
                        {1, 2},
                        {2, 0},
                        {3, 1},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"2", "4", "2", "3", "2", "2"};

                playChord(chordsTexts[9][3], chord, fingers);
                break;
            }

            case R.id.chord_F9: {
                byte[][] chord = {
                        {2, 2},
                        {3, 1},
                        {4, 3},
                        {5, 2}
                };

                String[] fingers = {"3", "2", "5", "4"};

                playChord(chordsTexts[9][4], chord, fingers);
                break;
            }

            case R.id.chord_Fm6: {
                byte[][] chord = {
                        {2, 4},
                        {3, 0},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"", "4", "3", "2"};

                playChord(chordsTexts[9][5], chord, fingers);
                break;
            }

            case R.id.chord_Fm7: {
                byte[][] chord = {
                        {0, 0},
                        {1, 2},
                        {2, 0},
                        {3, 0},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"2", "4", "2", "2", "2", "2"};

                playChord(chordsTexts[9][6], chord, fingers);
                break;
            }

            case R.id.chord_Fmaj7: {
                byte[][] chord = {
                        {2, 2},
                        {3, 1},
                        {4, 0},
                        {5, 4}
                };

                String[] fingers = {"4", "3", "2", ""};

                playChord(chordsTexts[9][7], chord, fingers);
                break;
            }

            case R.id.chord_Fdim: {
                byte[][] chord = {
                        {2, 4},
                        {3, 0},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"", "2", "", "3"};

                playChord(chordsTexts[9][8], chord, fingers);
                break;
            }

            case R.id.chord_Fplus: {
                byte[][] chord = {
                        {2, 2},
                        {3, 1},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"5", "4", "3", "2"};

                playChord(chordsTexts[9][9], chord, fingers);
                break;
            }

            case R.id.chord_Fsus: {
                byte[][] chord = {
                        {2, 2},
                        {3, 2},
                        {4, 0},
                        {5, 0}
                };

                String[] fingers = {"4", "3", "2", "2"};

                playChord(chordsTexts[9][10], chord, fingers);
                break;
            }

            //Key_F#
            case R.id.chord_Fsharp: {
                byte[][] chord = {
                        {0, 1},
                        {1, 3},
                        {2, 3},
                        {3, 2},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"2", "5", "4", "3", "2", "2"};

                playChord(chordsTexts[10][0], chord, fingers);
                break;
            }

            case R.id.chord_FsharpM: {
                byte[][] chord = {
                        {0, 1},
                        {1, 3},
                        {2, 3},
                        {3, 1},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"2", "4", "3", "2", "2", "2"};

                playChord(chordsTexts[10][1], chord, fingers);
                break;
            }

            case R.id.chord_Gb6: {
                byte[][] chord = {
                        {1, 3},
                        {2, 3},
                        {3, 2},
                        {4, 3}
                };

                String[] fingers = {"4", "3", "2", "5"};

                playChord(chordsTexts[10][2], chord, fingers);
                break;
            }

            case R.id.chord_Fsharp7: {
                byte[][] chord = {
                        {2, 3},
                        {3, 2},
                        {4, 1},
                        {5, 4}
                };

                String[] fingers = {"4", "3", "2", ""};

                playChord(chordsTexts[10][3], chord, fingers);
                break;
            }

            case R.id.chord_FsharpM6: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[10][4], chord, fingers);
                break;
            }

            case R.id.chord_FsharpM7: {
                byte[][] chord = {
                        {2, 1},
                        {3, 1},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"2", "2", "2", "2"};

                playChord(chordsTexts[10][5], chord, fingers);
                break;
            }

            case R.id.chord_Gbmaj7: {
                byte[][] chord = {
                        {2, 3},
                        {3, 2},
                        {4, 1},
                        {5, 0}
                };

                String[] fingers = {"5", "4", "3", "2"};

                playChord(chordsTexts[10][6], chord, fingers);
                break;
            }

            case R.id.chord_FsharpDim: {
                byte[][] chord = {
                        {2, 0},
                        {3, 1},
                        {4, 0},
                        {5, 1}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[10][7], chord, fingers);
                break;
            }

            case R.id.chord_GbPlus: {
                byte[][] chord = {
                        {2, 3},
                        {3, 2},
                        {4, 2},
                        {5, 1}
                };

                String[] fingers = {"5", "4", "3", "2"};

                playChord(chordsTexts[10][8], chord, fingers);
                break;
            }

            case R.id.chord_GbSus: {
                byte[][] chord = {
                        {2, 3},
                        {3, 3},
                        {4, 1},
                        {5, 1}
                };

                String[] fingers = {"4", "3", "2", "2"};

                playChord(chordsTexts[10][9], chord, fingers);
                break;
            }

            //Key_G
            case R.id.chord_G: {
                byte[][] chord = {
                        {0, 2},
                        {1, 1},
                        {2, 4},
                        {3, 4},
                        {4, 4},
                        {5, 2}
                };

                String[] fingers = {"3", "2", "", "", "", "4"};

                playChord(chordsTexts[11][0], chord, fingers);
                break;
            }

            case R.id.chord_G6: {
                byte[][] chord = {
                        {0, 2},
                        {1, 1},
                        {2, 4},
                        {3, 4},
                        {4, 4},
                        {5, 4}
                };

                String[] fingers = {"3", "2", "", "", "", ""};

                playChord(chordsTexts[11][1], chord, fingers);
                break;
            }

            case R.id.chord_G7: {
                byte[][] chord = {
                        {0, 2},
                        {1, 1},
                        {2, 4},
                        {3, 4},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"4", "3", "", "", "", "2"};

                playChord(chordsTexts[11][2], chord, fingers);
                break;
            }

            case R.id.chord_G9: {
                byte[][] chord = {
                        {0, 2},
                        {1, 4},
                        {2, 4},
                        {3, 1},
                        {4, 4},
                        {5, 0}
                };

                String[] fingers = {"4", "", "", "3", "", "2"};

                playChord(chordsTexts[11][3], chord, fingers);
                break;
            }

            case R.id.chord_Gm6: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 2},
                        {5, 2}
                };

                String[] fingers = {"2", "5", "4", "3"};

                playChord(chordsTexts[11][4], chord, fingers);
                break;
            }

            case R.id.chord_Gdim: {
                byte[][] chord = {
                        {2, 1},
                        {3, 2},
                        {4, 1},
                        {5, 2}
                };

                String[] fingers = {"2", "3", "2", "4"};

                playChord(chordsTexts[11][5], chord, fingers);
                break;
            }

            case R.id.chord_Gplus: {
                byte[][] chord = {
                        {2, 0},
                        {3, 4},
                        {4, 4},
                        {5, 2}
                };

                String[] fingers = {"2", "", "", "4"};

                playChord(chordsTexts[11][6], chord, fingers);
                break;
            }

            case R.id.chord_Gsus: {
                byte[][] chord = {
                        {2, 4},
                        {3, 4},
                        {4, 0},
                        {5, 2}
                };

                String[] fingers = {"", "", "2", "4"};

                playChord(chordsTexts[11][7], chord, fingers);
                break;
            }

        }
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

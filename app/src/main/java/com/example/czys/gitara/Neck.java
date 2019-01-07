package com.example.czys.gitara;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.billthefarmer.mididriver.MidiDriver;

public class Neck extends AppCompatActivity implements MidiDriver.OnMidiStartListener,
        View.OnTouchListener {

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
    TextView[][] notesButtonsClick = new TextView[6][5];
    boolean[][] isNotePlaying = new boolean[6][5];

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neck);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        midiDriver.start();

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
        sendPlayNote(notes);
        isNotePlaying[string][notePosition] = true;
    }

    private void stopNote(TextView notesButtonsClick, byte notes, int string, int notePosition) {
        ObjectAnimator.ofFloat(notesButtonsClick, View.ALPHA, 0.0f).setDuration(150).start();
        sendStopNote(notes);
        isNotePlaying[string][notePosition] = false;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(this.getClass().getName(), "Motion event: " + event);

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

            case R.id.returnArrow: {
                finish();
                break;
            }
        }
        return false;
    }
}

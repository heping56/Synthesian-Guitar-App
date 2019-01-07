package com.example.czys.gitara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Intent startChords;
    Intent startNeck;
    Intent startSongs;
    Button[] mainMenuButtons;
    int instrument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        final View decorView = this.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        startNeck = new Intent(MainMenu.this, Neck.class);
        startChords = new Intent(MainMenu.this, Chords.class);
        startSongs = new Intent(MainMenu.this, Songs.class);

        mainMenuButtons = new Button[6];

        mainMenuButtons[0] = findViewById(R.id.mainMenuButtonSong);
        mainMenuButtons[1] = findViewById(R.id.mainMenuButtonChords);
        mainMenuButtons[2] = findViewById(R.id.mainMenuButtonFreePlay);
        mainMenuButtons[3] = findViewById(R.id.mainMenuButtonGuitar);
        mainMenuButtons[4] = findViewById(R.id.mainMenuButtonGuitarNylon);
        mainMenuButtons[5] = findViewById(R.id.mainMenuButtonGuitarSteel);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        final View decorView = this.getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainMenuButtons[0].setBackgroundResource(R.drawable.mainmenubutton);
        mainMenuButtons[1].setBackgroundResource(R.drawable.mainmenubutton);
        mainMenuButtons[2].setBackgroundResource(R.drawable.mainmenubutton);
        mainMenuButtons[3].setBackgroundResource(R.drawable.mainmenubutton);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.mainMenuButtonSong: {
                mainMenuButtons[0].setBackgroundResource(R.drawable.mainmenubuttonclick);
                startActivity(startSongs);
                break;
            }

            case R.id.mainMenuButtonChords: {
                mainMenuButtons[1].setBackgroundResource(R.drawable.mainmenubuttonclick);
                startActivity(startChords);
                break;
            }

            case R.id.mainMenuButtonFreePlay: {
                mainMenuButtons[2].setBackgroundResource(R.drawable.mainmenubuttonclick);
                startActivity(startNeck);
                break;
            }

            case R.id.mainMenuButtonGuitar: {
                mainMenuButtons[3].setBackgroundResource(R.drawable.mainmenubuttonclick);
                mainMenuButtons[0].setVisibility(View.INVISIBLE);
                mainMenuButtons[1].setVisibility(View.INVISIBLE);
                mainMenuButtons[2].setVisibility(View.INVISIBLE);
                mainMenuButtons[4].setVisibility(View.VISIBLE);
                mainMenuButtons[5].setVisibility(View.VISIBLE);
                break;
            }

            case R.id.mainMenuButtonGuitarNylon: {
                mainMenuButtons[3].setBackgroundResource(R.drawable.mainmenubutton);
                mainMenuButtons[0].setVisibility(View.VISIBLE);
                mainMenuButtons[1].setVisibility(View.VISIBLE);
                mainMenuButtons[2].setVisibility(View.VISIBLE);
                mainMenuButtons[4].setVisibility(View.INVISIBLE);
                mainMenuButtons[5].setVisibility(View.INVISIBLE);
                instrument = 24;
                startNeck.putExtra("instrument", instrument);
                startChords.putExtra("instrument", instrument);
                startSongs.putExtra("instrument", instrument);
                mainMenuButtons[3].setText(R.string.guitar_nylon);
                break;
            }

            case R.id.mainMenuButtonGuitarSteel: {
                mainMenuButtons[3].setBackgroundResource(R.drawable.mainmenubutton);
                mainMenuButtons[0].setVisibility(View.VISIBLE);
                mainMenuButtons[1].setVisibility(View.VISIBLE);
                mainMenuButtons[2].setVisibility(View.VISIBLE);
                mainMenuButtons[4].setVisibility(View.INVISIBLE);
                mainMenuButtons[5].setVisibility(View.INVISIBLE);
                instrument = 25;
                startNeck.putExtra("instrument", instrument);
                startChords.putExtra("instrument", instrument);
                startSongs.putExtra("instrument", instrument);
                mainMenuButtons[3].setText(R.string.guitar_steel);
                break;
            }
        }
    }
}

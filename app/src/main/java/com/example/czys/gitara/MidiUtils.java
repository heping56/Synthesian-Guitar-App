package com.example.czys.gitara;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

public abstract class MidiUtils
{
    public static final String sdPath = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Synthesian/";


    public static MidiFile loadMIDI(String fileName){
        MidiFile mf= null;
        File input = new File(sdPath + fileName);

        try
        {
             mf = new MidiFile(input);
        }
        catch(IOException e)
        {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
        }
        return mf;
    }

    public static MidiFile loadMIDI(){
        MidiFile mf= null;
        File input = new File(sdPath + "_temp.mid");

        try
        {
            mf = new MidiFile(input);
        }
        catch(IOException e)
        {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
        }
        return mf;
    }

    public static MediaPlayer loadMIDIForMediaPlayer(String fileName) {
        File f = new File(sdPath + fileName);

        try {
            FileInputStream fis = new FileInputStream(f);
            FileDescriptor fd = fis.getFD();

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd);
            mediaPlayer.prepare();

            fis.close();
            return mediaPlayer;
        } catch (Exception e) {
            Log.e("MidiUtils", e.toString());
            return null;
        }

    }

    private static void saveMIDI(MidiFile midi, String fileName){
        File output = new File(sdPath, fileName);
        try {
            midi.writeToFile(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Long> getNoteOnTicks(MidiFile mf)
    {
        ArrayList<Long> ticks = new ArrayList<>();

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(NoteOn.class))
                ticks.add(E.getTick());
        }

        for (long tick: ticks
             ) {
            Log.d("Tick", "TickOn: " + tick);
        }
        return ticks;
    }

    public static float getBPM(MidiFile mf)
    {
        float BPM = 0.0f;

        MidiTrack T = mf.getTracks().get(0);

        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(Tempo.class))
            {
                Tempo tempo = (Tempo) E;
                BPM = tempo.getBpm();
                //tempo.setBpm(tempo.getBpm() / 2);
                Log.d("BPM", "BPM: " + tempo.getBpm());
                break;
            }
        }

        return BPM;
    }

    public static ArrayList<Byte> getNotes(MidiFile mf){
        ArrayList<Byte> notes = new ArrayList<>();

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(NoteOn.class)) {
                NoteOn noteValue = (NoteOn) E;
                notes.add((byte)noteValue.getNoteValue());
            }
        }

        for (long noteValue: notes
                ) {
            Log.d("Notes", "Notes: " + noteValue);
        }
        return notes;
    }

    public static int getResolution(MidiFile mf){
        int resolution = mf.getResolution();
        Log.d("Resolution", "Resolution: " + resolution);
        return resolution;
    }

    public static TimeSignature getTimeSignature(MidiFile mf){
        TimeSignature ts = null;

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(TimeSignature.class))
            {
                ts = (TimeSignature) E;
                Log.d("TimeSignature", ts.toString());
                break;
            }
        }

        return ts;
    }

    public static Tempo getTempo(MidiFile mf){
        Tempo tempo = null;

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(Tempo.class))
            {
                tempo = (Tempo) E;
                Log.d("TimeSignature", tempo.toString());
                break;
            }
        }

        return tempo;
    }

    public static int getTimeSignatureNumerator(MidiFile mf){
        int numerator = 0;

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(TimeSignature.class))
            {
                TimeSignature ts = (TimeSignature) E;
                numerator = ts.getNumerator();
                Log.d("Numerator", "" + ts.getNumerator());
                break;
            }
        }

        return numerator;
    }

    public static int getTimeSignatureDenominator(MidiFile mf){
        int denominator = 0;

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(TimeSignature.class))
            {
                TimeSignature ts = (TimeSignature) E;
                denominator = ts.getRealDenominator();
                Log.d("Numerator", "" + ts.getNumerator());
                break;
            }
        }

        return denominator;
    }

    public static String prepareTempMidi(String filename, int BPM){
        ArrayList<NoteOn> notesOn = new ArrayList<>();
        ArrayList<NoteOff> notesOff = new ArrayList<>();
        Tempo tempo = new Tempo();

        MidiFile mf = loadMIDI(filename);

        ArrayList<NoteOn> tempNotesOn = new ArrayList<>();
        ArrayList<MidiTrack> tempTracks = mf.getTracks();
        for (MidiTrack tempT: tempTracks
             ) {
            Iterator<MidiEvent> tempIt = tempT.getEvents().iterator();
            while(tempIt.hasNext()){
                MidiEvent E = tempIt.next();

                if(E.getClass().equals(NoteOn.class)) {
                    tempNotesOn.add((NoteOn) E);
                }
            }
        }
        for (NoteOn tempNoteOn:  tempNotesOn
             ) {
            if(tempNoteOn.getNoteValue() < 40 || tempNoteOn.getNoteValue() > 68)
                return "incompatibleNotes";
        }

        for (MidiTrack tempT: tempTracks
             ) {
            if(tempT.equals(tempTracks.get(0)))
                continue;
            for (MidiEvent tempEvent: tempT.getEvents()
                 ) {
                tempTracks.get(0).insertEvent(tempEvent);
            }
        }

        int resolution = mf.getResolution();
        if(resolution == 0)
            return "resolutionis0";

        MidiTrack T = mf.getTracks().get(0);
        Iterator<MidiEvent> it = T.getEvents().iterator();

        while(it.hasNext())
        {
            MidiEvent E = it.next();

            if(E.getClass().equals(NoteOn.class)) {
                notesOn.add((NoteOn) E);
            }
            else if(E.getClass().equals(NoteOff.class)) {
                notesOff.add((NoteOff) E);
            }
            else if(E.getClass().equals(Tempo.class)) {
                tempo = (Tempo) E;
            }
        }

        //remove events
        for (MidiEvent E : notesOn
                ) {
            T.removeEvent(E);
        }
        for (MidiEvent E : notesOff
                ) {
            T.removeEvent(E);
        }
        T.removeEvent(tempo);

        //add new noteOn, noteOff events with extra measure
        for (NoteOn note: notesOn
             ) {
            NoteOn newNoteOn = new NoteOn((note.getTick() / (resolution / 480)) + ((resolution / (resolution / 480)) * getTimeSignatureNumerator(mf)), note.getChannel(), note.getNoteValue(), note.getVelocity());
            T.insertEvent(newNoteOn);
        }
        for (NoteOff note: notesOff
                ) {
            NoteOff newNoteOff = new NoteOff((note.getTick() / (resolution / 480)) + ((resolution / (resolution / 480)) * getTimeSignatureNumerator(mf)) - 10, note.getChannel(), note.getNoteValue(), note.getVelocity());
            T.insertEvent(newNoteOff);
        }
        tempo.setBpm((float)BPM);
        T.insertEvent(tempo);

        mf.removeTrack(0);
        mf.addTrack(T);

        mf.setResolution(480);

        saveMIDI(mf, "_temp.mid");
        return null;
    }

    public static void playTempMidi() {
        Songs.playerTemp = loadMIDIForMediaPlayer("_temp.mid");
        Songs.playerTemp.start();
    }

    public static void stopTempMidi() {
        if(Songs.playerTemp.isPlaying())
            Songs.playerTemp.stop();
    }

    public static void prepareMetronome() {
        MidiFile mf = loadMIDI();

        MidiFile metronomeMidi = new MidiFile();
        MidiTrack track = new MidiTrack();
        ProgramChange programChange = new ProgramChange(0, 0, 117);

        track.insertEvent(getTempo(mf));
        track.insertEvent(getTimeSignature(mf));
        track.insertEvent(programChange);

        for (long i = 0; i<mf.getTracks().get(0).getLengthInTicks(); i = i + (mf.getResolution() * 4 / getTimeSignatureDenominator(mf))){
            track.insertNote(0,50,127, i, mf.getResolution());
        }

        Log.d("LengthInTicks", "" + mf.getTracks().get(0).getLengthInTicks());

        metronomeMidi.addTrack(track);

        saveMIDI(metronomeMidi, "_metronome.mid");
    }

    public static void playMetronome() {
        Songs.playerMetronome = loadMIDIForMediaPlayer("_metronome.mid");
        Songs.playerMetronome.start();
    }

    public static void stopMetronome() {
        if(Songs.playerMetronome.isPlaying())
            Songs.playerMetronome.stop();
    }
}

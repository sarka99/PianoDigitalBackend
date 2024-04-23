package com.piano.PianoDigital.Utils;

import java.util.HashMap;
import java.util.Map;

public class MidiNoteConverter {
    private static final Map<Integer, String> NOTE_NAMES = new HashMap<>();
    static {
        // Mapping MIDI note numbers to note names
        NOTE_NAMES.put(0, "C");
        NOTE_NAMES.put(1, "C#");
        NOTE_NAMES.put(2, "D");
        NOTE_NAMES.put(3, "D#");
        NOTE_NAMES.put(4, "E");
        NOTE_NAMES.put(5, "F");
        NOTE_NAMES.put(6, "F#");
        NOTE_NAMES.put(7, "G");
        NOTE_NAMES.put(8, "G#");
        NOTE_NAMES.put(9, "A");
        NOTE_NAMES.put(10, "A#");
        NOTE_NAMES.put(11, "B");
    }
    public static String convertToNoteName(int midiNote) {
        int note = midiNote % 12; // Get the note index within an octave
        int octave = (midiNote / 12) - 1; // Get the octave number

        return NOTE_NAMES.get(note) + (octave < 0 ? "" : octave);
    }
}

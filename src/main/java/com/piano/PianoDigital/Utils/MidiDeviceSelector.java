package com.piano.PianoDigital.Utils;

import javax.sound.midi.*;

public class MidiDeviceSelector {

    public static MidiDevice getKeyboardDevice() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);
            // Check if the device is a keyboard and has transmitters
            if (device.getMaxTransmitters() != 0 && info.getName().contains("RD")) {
                return device;
            }
        }
        return null; // Keyboard not found
    }

}

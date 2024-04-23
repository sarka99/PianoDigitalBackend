package com.piano.PianoDigital.service.implementations;

import com.piano.PianoDigital.Utils.MidiDeviceSelector;
import com.piano.PianoDigital.Utils.MidiNoteConverter;
import com.piano.PianoDigital.db.entity.Recording;
import com.piano.PianoDigital.db.entity.User;
import com.piano.PianoDigital.db.repository.RecordingRepository;
import com.piano.PianoDigital.db.repository.UserRepository;
import com.piano.PianoDigital.service.interfaces.IRecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.*;

@Service
public class RecordingServiceImpl implements IRecordingService {

    @Autowired
    private RecordingRepository recordingRepository;
    @Autowired
    private UserRepository userRepository;

    private Sequencer sequencer;

    @Override
    public void stopRecording() throws Exception {
        try {
            if (sequencer == null || !sequencer.isRecording()) {
                throw new Exception("No recording in progress to stop.");
            }

            // Stop recording
            sequencer.stopRecording();
            System.out.println("Recording stopped.");

            // Get the recorded sequence
            Sequence recordedSequence = sequencer.getSequence();

            // Save the recorded sequence to a MIDI file
            File midiFile = new File("C:/Users/sargo/Desktop/recorded_track.mid");
            int[] fileTypes = MidiSystem.getMidiFileTypes(recordedSequence);
            if (fileTypes.length > 0) {
                MidiSystem.write(recordedSequence, fileTypes[0], midiFile);
                System.out.println("MIDI file saved as: " + midiFile.getAbsolutePath());
            } else {
                throw new Exception("No valid MIDI file types available for saving.");
            }

            // Optionally, you can upload the MIDI file to the database or perform any other operations here

        } catch (Exception e) {
            throw new Exception("Error stopping recording: " + e.getMessage());
        }
    }


    @Override
    public void startRecording() throws Exception {
        try {
            // Get the MIDI keyboard device
            MidiDevice keyboardDevice = MidiDeviceSelector.getKeyboardDevice();
            if (keyboardDevice == null) {
                throw new Exception("MIDI keyboard not found.");
            }

            // Open the MIDI keyboard device
            keyboardDevice.open();

            // Get a sequencer without a default device
            sequencer = MidiSystem.getSequencer(false);

            // Open the sequencer
            sequencer.open();

            // Set the sequence
            sequencer.setSequence(new Sequence(Sequence.PPQ, 24));

            // Get the transmitter from the MIDI keyboard device
            Transmitter transmitter = keyboardDevice.getTransmitter();

            // Get the receiver from the sequencer
            Receiver receiver = sequencer.getReceiver();

            // Connect the transmitter to the receiver
            transmitter.setReceiver(receiver);

            // Record-enable the track
            sequencer.recordEnable(sequencer.getSequence().createTrack(), -1);

            // Start recording
            sequencer.startRecording();
            System.out.println("Recording started...");

        } catch (Exception e) {
            throw new Exception("Error starting recording: " + e.getMessage());
        }
    }

    @Override
    public Recording saveTeacherRecording(MultipartFile file, String title, String description, Long recordedById, Long assignedById,
                                          Long original_track_id) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if (fileName.contains("..")){
                throw new Exception("Filename contains invalid path sequence");
            }
            // Fetch recordedBy and assignedBy users from repository
            User recordedBy = userRepository.findById(recordedById).orElseThrow(() -> new IllegalArgumentException("Recorded by user not found"));
            User assignedBy = userRepository.findById(assignedById).orElseThrow(() -> new IllegalArgumentException("Assigned by user not found"));
            Recording recording = new Recording(title,
                    description,
                    recordedBy,
                    assignedBy,
                    LocalDateTime.now(),
                    file.getBytes(),
                    fileName,
                    file.getContentType());
            recording.setOriginalRecordingId(original_track_id);
            return recordingRepository.save(recording);


        }catch (Exception e){
            throw new Exception("Could not save file:" + fileName);
        }

    }

    @Override
    public Recording saveStudentRecording(MultipartFile file, String title, String description, Long recordedById, Long assignedById, Long original_track_id) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if (fileName.contains("..")){
                throw new Exception("Filename contains invalid path sequence");
            }
            // Fetch recordedBy and assignedBy users from repository
            User recordedBy = userRepository.findById(recordedById).orElseThrow(() -> new IllegalArgumentException("Recorded by user not found"));
            User assignedBy = userRepository.findById(assignedById).orElseThrow(() -> new IllegalArgumentException("Assigned by user not found"));
            Recording studentsRecording = new Recording(title,
                    description,
                    recordedBy,
                    assignedBy,
                    LocalDateTime.now(),
                    file.getBytes(),
                    fileName,
                    file.getContentType());
            studentsRecording.setOriginalRecordingId(original_track_id);
            //Save the recording to db as a recording row

            //Do feedback, get the saved studentsRecording --> compare to the original
            //FindRecordingByRecordedBYID now we have the students track, from it extract the original track and then compare them.
           // recordingRepository.save(studentsRecording);
            Long originalRecordingId = studentsRecording.getOriginalRecordingId();
            Recording originalRecording = recordingRepository.findRecordingById(originalRecordingId);
            System.out.println("The students recording to be compared:" + studentsRecording);
            System.out.println("The original recording to be compared:" + originalRecording);
            compareTracksNotes(studentsRecording,originalRecording);

            return null;



        }catch (Exception e){
            throw new Exception("Could not save file:" + fileName);
        }
    }
    private void compareTracksNotes(Recording studentRecording,Recording originalRecording) throws InvalidMidiDataException, IOException {
        // Get the MIDI sequences from student and original recordings
        Sequence studentSequence = MidiSystem.getSequence(new ByteArrayInputStream(studentRecording.getMidiFileData()));
        Sequence originalSequence = MidiSystem.getSequence(new ByteArrayInputStream(originalRecording.getMidiFileData()));

        // Extract notes from both sequences
        List<Integer> studentNotes = extractNotes(studentSequence);
        List<Integer> originalNotes = extractNotes(originalSequence);
        List<String> wrongNotes = new ArrayList<>();

        // Compare notes
        int correctNotes = 0;
        int missedNotes = 0;
        int extraNotes = 0;
        int correctNotesPercentage = 0;

        for (Integer note : studentNotes) {
            if (originalNotes.contains(note)) {
                correctNotes++;
            } else {
                extraNotes++;
            }
        }
        correctNotesPercentage = (int) ((double) correctNotes / originalNotes.size() * 100);
        wrongNotes = identifyWrongNotes(studentNotes,originalNotes);
        missedNotes = originalNotes.size() - correctNotes;


        // Print comparison results
        System.out.println("Correct Notes: " + correctNotes);
        System.out.println("Missed Notes: " + missedNotes);
        System.out.println("Extra Notes: " + extraNotes);
        System.out.println("Wrong notes: " + wrongNotes);
        System.out.println("Note Accuracy: " + correctNotesPercentage+"%");
    }
    private List<Integer> extractNotes(Sequence sequence) {
        List<Integer> notes = new ArrayList<>();

        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        notes.add(sm.getData1()); // Adding the note value
                    }
                }
            }
        }

        return notes;
    }
    private List<String> identifyWrongNotes(List<Integer> studentNotes, List<Integer> originalNotes){
        List<String> wrongNotes = new ArrayList<>();
        for (int i = 0; i < studentNotes.size(); i++) {
            if (!studentNotes.get(i).equals(originalNotes.get(i))) {
                wrongNotes.add(MidiNoteConverter.convertToNoteName(studentNotes.get(i))); // Add wrong note to the list

            }
        }
        return wrongNotes;
    }

    @Override
    public List<Recording> getRecordingsByUserId(Long userId) {
        return recordingRepository.findByRecordedById(userId);
    }

    @Override
    public Recording getRecordingById(Long recordingId) throws Exception {

        return recordingRepository
                .findById(recordingId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + recordingId));
    }

    @Override
    public String getRecordingDownloadURL(Long recordingId) throws Exception {
        Recording recording = getRecordingById(recordingId);
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/recordings/")
                .path(String.valueOf(recording.getId()))
                .path("/download")
                .toUriString();
    }
    private Recording saveRecordingToDb(byte[] midiSeq, String title, String description, Long recordedById, Long assignedById,
                                   Long original_track_id) throws IOException {
        //will be called in stopRecording service method.
        User recordedBy = userRepository.findById(recordedById).orElseThrow(() -> new IllegalArgumentException("Recorded by user not found"));
        User assignedBy = userRepository.findById(assignedById).orElseThrow(() -> new IllegalArgumentException("Assigned by user not found"));
        Recording recording = new Recording(title,
                description,
                recordedBy,
                assignedBy,
                LocalDateTime.now(),
                midiSeq,
                "dynamic_recording1",
                "audio/midi"
                );
        recording.setOriginalRecordingId(original_track_id);
        return recordingRepository.save(recording);
    }
}

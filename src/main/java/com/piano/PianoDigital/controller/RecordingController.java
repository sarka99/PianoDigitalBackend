package com.piano.PianoDigital.controller;


import com.piano.PianoDigital.db.entity.Recording;
import com.piano.PianoDigital.service.interfaces.IRecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/recordings")
public class RecordingController {

    @Autowired
    private IRecordingService recordingService;


    @PostMapping("/startRecording")
    public ResponseEntity<String> startRecording() {
        try {
            recordingService.startRecording();
            return ResponseEntity.ok("Recording started successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting recording: " + e.getMessage());
        }
    }
    @PostMapping("/stopRecording")
    public void stopRecording() throws Exception {
        recordingService.stopRecording();
    }
    @PostMapping("/uploadTeachersRecording")
    public ResponseEntity<Recording> saveTeacherRecording(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description,
                                                          @RequestParam("recordedBy") Long recordedById,
                                                          @RequestParam("assignedBy") Long assignedById,
                                                          @RequestParam("originalRecordingId") Long original_recording_id) throws Exception {

        Recording recording = null;
        String downloadMidiURL = "";
        recording = recordingService.saveTeacherRecording(file,title,description,recordedById,assignedById, original_recording_id);

        downloadMidiURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(String.valueOf((Long)recording.getId()))
                .toUriString();
        System.out.println(recording);
        return ResponseEntity.status(HttpStatus.CREATED).body(recording);

    }
    @PostMapping("/uploadStudentsRecording")
    public ResponseEntity<Recording> saveStudentRecording(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("recordedBy") Long recordedById,
                                                   @RequestParam("assignedBy") Long assignedById,
                                                   @RequestParam("originalRecordingId") Long original_recording_id) throws Exception {

        Recording recording = null;
        String downloadMidiURL = "";
        recording = recordingService.saveStudentRecording(file,title,description,recordedById,assignedById, original_recording_id);

        downloadMidiURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(String.valueOf((Long)recording.getId()))
                .toUriString();
        System.out.println(recording);
        return ResponseEntity.status(HttpStatus.CREATED).body(recording);

    }
    @GetMapping("/download/{recordingId}")
    public ResponseEntity<Resource> downloadMidiFileByRecordingId(@PathVariable Long recordingId) throws Exception {
        //Once the api endpoint is called from the frontend, the midi file will be downloaded
        Recording recording = null;
        recording = recordingService.getRecordingById(recordingId);
        System.out.println("Downloading midi file by recording id:" + recording);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/midi")) // Set content type for MIDI file
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "recording; filename=\"" + recording.getFileName() + "\"")
                .body(new ByteArrayResource(recording.getMidiFileData()));

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recording>> getRecordingsByUserId(@PathVariable("userId") Long userId) {
        List<Recording> recordings = recordingService.getRecordingsByUserId(userId);
        if (recordings.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(recordings);
        }
    }
}

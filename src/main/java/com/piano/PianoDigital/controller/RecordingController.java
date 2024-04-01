package com.piano.PianoDigital.controller;


import com.piano.PianoDigital.db.entity.Recording;
import com.piano.PianoDigital.service.interfaces.IRecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recordings")
public class RecordingController {

    @Autowired
    private IRecordingService recordingService;

    @PostMapping("/upload")
    public ResponseEntity<Recording> uploadMidiFile(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("description") String description,
                                                    @RequestParam("recordedBy") Long recordedById,
                                                    @RequestParam("assignedBy") Long assignedById) throws Exception {

        Recording recording = null;

        recording = recordingService.saveMidiRecording(file,title,description,recordedById,assignedById);
        System.out.println(recording.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(recording);

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recording>> getRecordingsByUserId(@PathVariable("userId") Long userId) {
        List<Recording> recordings = recordingService.getRecordingsByUserId(userId);
        if (recordings.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            for (Recording r : recordings){
                System.out.println(r.toString());
            }
            return ResponseEntity.ok(recordings);
        }
    }



}

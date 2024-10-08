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

    @PostMapping("/upload")
    public ResponseEntity<Recording> saveRecording(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("recordedBy") Long recordedById,
                                                   @RequestParam("assignedBy") Long assignedById) throws Exception {

        Recording recording = null;
        String downloadMidiURL = "";
        recording = recordingService.saveRecording(file,title,description,recordedById,assignedById);

        downloadMidiURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                                .path(String.valueOf((Long)recording.getId()))
                                        .toUriString();


        System.out.println(recording.toString());



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

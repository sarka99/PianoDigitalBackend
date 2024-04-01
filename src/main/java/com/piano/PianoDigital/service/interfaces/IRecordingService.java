package com.piano.PianoDigital.service.interfaces;

import com.piano.PianoDigital.db.entity.Recording;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRecordingService {

    Recording saveMidiRecording(MultipartFile file, String title, String description, Long recordedById, Long assignedById) throws Exception;

    List<Recording> getRecordingsByUserId(Long userId);
}

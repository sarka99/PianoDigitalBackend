package com.piano.PianoDigital.service.interfaces;

import com.piano.PianoDigital.db.entity.Recording;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRecordingService {


    void stopRecording() throws Exception;
    void startRecording() throws Exception;
    Recording saveTeacherRecording(MultipartFile file, String title, String description, Long recordedById, Long assignedById,
                                   Long original_track_id) throws Exception;
    Recording saveStudentRecording(MultipartFile file, String title, String description, Long recordedById, Long assignedById,
                            Long original_track_id) throws Exception;


    List<Recording> getRecordingsByUserId(Long userId);

    Recording getRecordingById(Long recordingId) throws Exception;

    String getRecordingDownloadURL(Long recordingId) throws Exception;

}

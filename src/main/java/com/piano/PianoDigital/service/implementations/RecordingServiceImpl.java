package com.piano.PianoDigital.service.implementations;

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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordingServiceImpl implements IRecordingService {

    @Autowired
    private RecordingRepository recordingRepository;
    @Autowired
    private UserRepository userRepository;



    @Override
    public Recording saveRecording(MultipartFile file, String title, String description, Long recordedById, Long assignedById,
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
}

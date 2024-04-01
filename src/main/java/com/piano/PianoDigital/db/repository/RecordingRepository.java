package com.piano.PianoDigital.db.repository;

import com.piano.PianoDigital.db.entity.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository  extends JpaRepository<Recording, Long> {
    // query to find a track based on who recorded it (their id)
    List<Recording> findByRecordedById(Long userId);

}

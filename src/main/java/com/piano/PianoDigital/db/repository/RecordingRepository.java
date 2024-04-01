package com.piano.PianoDigital.db.repository;

import com.piano.PianoDigital.db.entity.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository  extends JpaRepository<Recording, Long> {
    List<Recording> findRecordingsByRecordedById(Long userId);

}

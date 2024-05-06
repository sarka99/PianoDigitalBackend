package com.piano.PianoDigital.db.repository;

import com.piano.PianoDigital.db.entity.Recording;
import com.piano.PianoDigital.db.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {
}

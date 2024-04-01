package com.piano.PianoDigital.db.repository;

import com.piano.PianoDigital.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

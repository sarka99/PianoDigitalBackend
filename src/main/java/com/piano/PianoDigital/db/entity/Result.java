package com.piano.PianoDigital.db.entity;

import com.piano.PianoDigital.db.entity.Recording;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "results")
@Setter
@Getter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_recording_id", nullable = false)
    private Recording studentRecording;

    @Column(name = "tempo", nullable = false)
    private Integer tempo;

    @Column(name = "wrong_notes_played", columnDefinition = "json")
    private String wrongNotesPlayed;

    @Column(name = "correct_percentage", columnDefinition = "json")
    private Float correctPercentage;

    @Column(name = "dynamic", columnDefinition = "json")
    private Float dynamic;
    // Constructors, getters, and setters

}
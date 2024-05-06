package com.piano.PianoDigital.db.entity;

import com.piano.PianoDigital.db.entity.Recording;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


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

    @Column(name = "wrong_notes_played")
    @ElementCollection
    private List<String> wrongNotesPlayed;

    @Column(name = "note_accuracy_percentage")
    private Double noteAccuracyPercentage;

    @Column(name = "number_correct_notes")
    private Integer numberOfCorrectNotes;

    @Column(name = "number_missed_notes")
    private Integer numberOfMissedNotes;

    @Column(name = "number_extra_notes")
    private Integer numberOfExtraNotes;

    @Column(name = "teacher_tempo", nullable = false)
    private Double teacherTempoBPM;

    @Column(name = "student_tempo", nullable = false)
    private Double studentTempoBPM;


    @Column(name = "teacher_dynamic")
    private Integer teacherDynamic;

    @Column(name = "student_dynamic")
    private Integer studentDynamic;
    // Constructors, getters, and setters

}
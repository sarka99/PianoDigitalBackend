package com.piano.PianoDigital.db.entity;

import com.piano.PianoDigital.db.entity.Recording;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

import java.util.List;


@Entity
@Table(name = "results")
@Setter
@Getter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_recording_id", nullable = false)
    private Recording studentRecording;

    @ElementCollection
    @CollectionTable(name = "result_wrong_notes_played", joinColumns = @JoinColumn(name = "result_id"))
    @Column(name = "wrong_note")
    private List<String> wrongNotesPlayed;

    @Column(name = "note_accuracy_percentage")
    private Double noteAccuracyPercentage;

    @Column(name = "number_correct_notes")
    private Integer numberOfCorrectNotes;

    @Column(name = "number_missed_notes")
    private Integer numberOfMissedNotes;

    @Column(name = "number_extra_notes")
    private Integer numberOfExtraNotes;

    @Column(name = "teacher_tempo")
    private Double teacherTempoBPM;

    @Column(name = "student_tempo")
    private Double studentTempoBPM;


    @Column(name = "teacher_dynamic")
    private Integer teacherDynamic;

    @Column(name = "student_dynamic")
    private Integer studentDynamic;
    // Constructors, getters, and setters

    public Result(Recording studentRecording, List<String> wrongNotesPlayed, Double noteAccuracyPercentage,
                  Integer numberOfCorrectNotes, Integer numberOfMissedNotes, Integer numberOfExtraNotes,
                  Double teacherTempoBPM, Double studentTempoBPM, Integer teacherDynamic, Integer studentDynamic) {
        this.studentRecording = studentRecording;
        this.wrongNotesPlayed = wrongNotesPlayed;
        this.noteAccuracyPercentage = noteAccuracyPercentage;
        this.numberOfCorrectNotes = numberOfCorrectNotes;
        this.numberOfMissedNotes = numberOfMissedNotes;
        this.numberOfExtraNotes = numberOfExtraNotes;
        this.teacherTempoBPM = teacherTempoBPM;
        this.studentTempoBPM = studentTempoBPM;
        this.teacherDynamic = teacherDynamic;
        this.studentDynamic = studentDynamic;
    }

    public Result() {

    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", studentRecording=" + studentRecording +
                ", wrongNotesPlayed=" + wrongNotesPlayed +
                ", noteAccuracyPercentage=" + noteAccuracyPercentage +
                ", numberOfCorrectNotes=" + numberOfCorrectNotes +
                ", numberOfMissedNotes=" + numberOfMissedNotes +
                ", numberOfExtraNotes=" + numberOfExtraNotes +
                ", teacherTempoBPM=" + teacherTempoBPM +
                ", studentTempoBPM=" + studentTempoBPM +
                ", teacherDynamic=" + teacherDynamic +
                ", studentDynamic=" + studentDynamic +
                '}';
    }
}
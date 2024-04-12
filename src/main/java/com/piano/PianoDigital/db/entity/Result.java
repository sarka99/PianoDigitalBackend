package com.piano.PianoDigital.db.entity;

import com.piano.PianoDigital.db.entity.Recording;
import jakarta.persistence.*;


@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_recording_id", nullable = false)
    private Recording studentRecording;

    @Column(name = "comparison_score", nullable = false)
    private Float comparisonScore;

    @Column(name = "additional_metrics", columnDefinition = "json")
    private String additionalMetrics;

    // Constructors, getters, and setters
}
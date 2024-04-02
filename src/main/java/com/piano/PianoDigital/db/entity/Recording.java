package com.piano.PianoDigital.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "recordings")
@Setter
@Getter
public class Recording {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "recorded_by_id", nullable = false)
    private User recordedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_by_id", nullable = false)
    private User assignedBy;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @Lob
    @Column(nullable = false)
    private byte[] midiFileData;

    private String fileName;
    private String fileType;

    @Column(name = "original_recording_id")
    private Long originalRecordingId;
    public Recording() {
    }

    public Recording(String title, String description, User recordedBy, User assignedBy, LocalDateTime uploadDate, byte[] midiFileData) {

        this.title = title;
        this.description = description;
        this.recordedBy = recordedBy;
        this.assignedBy = assignedBy;
        this.uploadDate = uploadDate;
        this.midiFileData = midiFileData;
    }

    public Recording(String title, String description, User recordedBy, User assignedBy, LocalDateTime uploadDate, byte[] midiFileData, String fileName, String fileType) {
        this.title = title;
        this.description = description;
        this.recordedBy = recordedBy;
        this.assignedBy = assignedBy;
        this.uploadDate = uploadDate;
        this.midiFileData = midiFileData;
        this.fileName = fileName;
        this.fileType = fileType;
    }



    @Override
    public String toString() {
        return "Recording{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", recordedBy=" + recordedBy +
                ", assignedBy=" + assignedBy +
                ", uploadDate=" + uploadDate +
                ", midiFileData=" + Arrays.toString(midiFileData) +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}

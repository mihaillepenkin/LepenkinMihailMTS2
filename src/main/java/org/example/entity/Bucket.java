package org.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "bucket")
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucketId")
    private long bucketId;

    @Setter
    private String bucketName;

    @OneToMany(mappedBy = "bucket")
    private List<File> file;

    protected Bucket () {}

    public Bucket(String bucketName, List<File> file) {
        this.bucketName = bucketName;
        this.file = file;
    }
}
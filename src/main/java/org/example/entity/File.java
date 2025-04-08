package org.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;

    @Setter
    private String fileName;

    @ManyToOne
    @Setter
    @JoinColumn(name = "bucketId", referencedColumnName = "bucketId")
    private Bucket bucket;

    protected File () {}

    public File(String fileName, Bucket bucket) {
        this.fileName = fileName;
        this.bucket = bucket;
    }
}

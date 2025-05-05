package org.example.repository;

import org.example.entity.File;
import org.example.entity.Bucket;
import jakarta.transaction.Transactional;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class RepositoryTest {

    @Container @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17");

    @Autowired FileRepository filesRepository;
    @Autowired BucketRepository bucketRepository;

    @Test
    void findFileByNameEquals() throws FileNotFoundException {
        ArrayList<File> files = new ArrayList<>();
        Bucket testBucket = bucketRepository.save(new Bucket("Company", files));
        File testFile =
                filesRepository.save(new File("Yandex", testBucket));
        testBucket.getFile().add(testFile);

        File responceFile = filesRepository.findByFileNameEquals("Yandex").orElseThrow();

        assertEquals(testFile, responceFile);
    }

    @Test
    void findBucketByNameEquals() throws BucketNotFoundException {
        ArrayList<File> files = new ArrayList<>();
        Bucket testBucket = bucketRepository.save(new Bucket("Company", files));
        File testFile =
                filesRepository.save(new File("Yandex", testBucket));
        testBucket.getFile().add(testFile);

        Bucket responceBucket = bucketRepository.findByBucketNameEquals("Company").orElseThrow();

        assertEquals(testBucket, responceBucket);
    }

    @Test
    void findBucketByIdEquals() throws BucketNotFoundException {
        ArrayList<File> files = new ArrayList<File>();

        Bucket testBucket = new Bucket("Company", files);
        File testFile = new File("Yandex", testBucket);
        testBucket.getFile().add(testFile);
        bucketRepository.save(testBucket);
        filesRepository.save(testFile);
        Bucket responceBucket = bucketRepository.findByBucketIdEquals(testFile.getBucket().getBucketId()).orElseThrow();

        assertEquals(testBucket, responceBucket);
    }
}
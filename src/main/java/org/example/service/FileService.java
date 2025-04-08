package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Bucket;
import org.example.entity.File;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.repository.BucketRepository;
import org.example.repository.FileRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileRepository fileRepository;
    private final BucketRepository bucketRepository;

    // At Least Once
    @Retryable(value = FileNotFoundException.class, maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public void renameFile(String oldFileName, String newFileName) throws FileNotFoundException, BucketNotFoundException {
        //с вероятностью 50 процентов запрос будет обращен к несуществующему файлу
        if (Math.random() < 0.2) {
            oldFileName = oldFileName + ((int) (Math.random() * Math.random() * 158793107) + "");
        }
        log.info("Функция переименования файла в сервисе");
        File file = fileRepository.findByFileNameEquals(oldFileName).orElseThrow(FileNotFoundException::new);
        Bucket bucket = bucketRepository.findByBucketIdEquals(file.getBucket().getBucketId()).orElseThrow(BucketNotFoundException::new);
        bucket.getFile().remove(file);
        file.setFileName(newFileName);
        bucket.getFile().add(file);
        fileRepository.save(file);
        bucketRepository.save(bucket);
    }

    @Async
    public void replaceFile(String oldBucketName, String FileName, String newBucketName) throws FileNotFoundException, BucketNotFoundException {
        log.info("Функция перемещения в сервисе");
        File file = fileRepository.findByFileNameEquals(FileName).orElseThrow(FileNotFoundException::new);
        Bucket oldBucket = bucketRepository.findByBucketNameEquals(oldBucketName).orElseThrow(BucketNotFoundException::new);
        Bucket newBucket = bucketRepository.findByBucketNameEquals(newBucketName).orElseThrow(BucketNotFoundException::new);
        oldBucket.getFile().remove(file);
        newBucket.getFile().add(file);
        file.setBucket(newBucket);
        bucketRepository.save(oldBucket);
        bucketRepository.save(newBucket);
        fileRepository.save(file);

    }
    @Cacheable(cacheNames = {"deleteFile"}, key = "{#FileName}")
    public void deleteFile(String FileName) throws FileNotFoundException, BucketNotFoundException {
        log.info("Функция удаления файла в сервисе");
        File file = fileRepository.findByFileNameEquals(FileName).orElseThrow(FileNotFoundException::new);
        Bucket bucket = bucketRepository.findByBucketIdEquals(file.getBucket().getBucketId()).orElseThrow(BucketNotFoundException::new);
        bucket.getFile().remove(file);
        bucketRepository.save(bucket);
        fileRepository.delete(file);
    }

    @Cacheable(cacheNames = {"createFile"}, key = "{#FileName}")
    public void createFile(String BucketName, String FileName) throws BucketNotFoundException {
        log.info("Функция создания файла в сервисе");
        Bucket bucket = bucketRepository.findByBucketNameEquals(BucketName).orElseThrow(BucketNotFoundException::new);
        File file = new File(FileName, bucket);
        bucket.getFile().add(file);
        bucketRepository.save(bucket);
        fileRepository.save(file);
    }
}
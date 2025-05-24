package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Bucket;
import org.example.entity.File;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.repository.BucketRepository;
import org.example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BucketService {

    private final BucketRepository bucketRepository;
    private final FileRepository fileRepository;
    private final Set<String> processedIds = ConcurrentHashMap.newKeySet();
    @Cacheable(cacheNames = {"renameBucket"}, key = "{#newBucketName}")
    public void renameBucket(String oldBucketName, String newBucketName) throws BucketNotFoundException {
        if (!processedIds.add(oldBucketName)) {
            log.info("Уже переименован");
            return;
        }
        log.info("Функция переименования бакета в сервисе");
        Bucket bucket = bucketRepository.findByBucketNameEquals(oldBucketName).orElseThrow(BucketNotFoundException::new);
        bucket.setBucketName(newBucketName);
        for (File el: bucket.getFile()) {
            el.setBucket(bucket);
        }
        bucket.setBucketName(newBucketName);
        bucketRepository.save(bucket);
    }

    @Cacheable(cacheNames = {"deleteBucket"}, key = "{#BucketName}")
    public void deleteBucket(String BucketName) throws BucketNotFoundException {
        log.info("Функция удаления бакета в сервисе");
        Bucket bucket = bucketRepository.findByBucketNameEquals(BucketName).orElseThrow(BucketNotFoundException::new);
        for (File el: bucket.getFile()) {
            fileRepository.delete(el);
        }
        bucketRepository.delete(bucket);
    }

    @Cacheable(cacheNames = {"createBucket"}, key = "{#BucketName}")
    public void createBucket(String BucketName) {
        log.info("Функция создания бакета в сервисе");
        Bucket bucket = new Bucket(BucketName, new ArrayList<>());
        bucketRepository.save(bucket);
    }
}
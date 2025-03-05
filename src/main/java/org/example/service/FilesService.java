package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.repository.FilesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesService {
    private final FilesRepository filesRepository = new FilesRepository();

    // At Least Once
    @Retryable(value = FileNotFoundException.class, maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public String renameFile(String oldFileName, String newFileName) throws FileNotFoundException {
        //с вероятностью 50 процентов запрос будет обращен к несуществующему файлу
        if (Math.random() < 0.2) {
            oldFileName = oldFileName + ((int) (Math.random() * Math.random() * 158793107) + "");
        }
        log.info("Функция переименования файла");
        return filesRepository.renameFile(oldFileName, newFileName);
    }

    // Exactly Once
    private final Set<String> processedIds = ConcurrentHashMap.newKeySet();
    @Cacheable(cacheNames = {"renameBucket"}, key = "{#newBucketName}")
    public String renameBucket(String oldBucketName, String newBucketName) throws BucketNotFoundException {
        if (!processedIds.add(oldBucketName)) {
            return "Уже переименован";
        }
        log.info("Функция переименования бакета");
        return filesRepository.renameBucket(oldBucketName, newBucketName);
    }

    @Async
    public void replaceFile(String oldBucketName, String FileName, String newBucketName) throws FileNotFoundException, BucketNotFoundException {
            log.info("Функция перемещения");
            filesRepository.replaceFile(oldBucketName, FileName, newBucketName);
    }
    @Cacheable(cacheNames = {"deliteFile"}, key = "{#FileName}")
    public String deleteFile(String FileName) throws FileNotFoundException {
            log.info("Функция удаления файла");
            return filesRepository.deleteFile(FileName);
    }
    @Cacheable(cacheNames = {"deliteBucket"}, key = "{#BucketName}")
    public String deleteBucket(String BucketName) throws BucketNotFoundException {
            log.info("Функция удаления бакета");
            return filesRepository.deleteBucket(BucketName);
    }
}
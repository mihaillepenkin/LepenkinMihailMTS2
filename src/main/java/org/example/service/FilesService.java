package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.repository.FilesRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesService {
    private final FilesRepository filesRepository;

    public String renameFile(String oldFileName, String newFileName) throws FileNotFoundException {
        if (oldFileName.equals("notFoundFileName")) {
            throw new FileNotFoundException(String.format("file not found"));
        } else {
            log.info("Функция переименования файла");
            return filesRepository.renameFile(oldFileName, newFileName);
        }
    }

    public String renameBucket(String oldBucketName, String newBucketName) throws BucketNotFoundException {
        if (oldBucketName.equals("notFoundOldBucketName")) {
            throw new BucketNotFoundException(String.format("bucket not found"));
        } else {
            log.info("Функция переименования бакета");
            return filesRepository.renameBucket(oldBucketName, newBucketName);
        }
    }

    public String replaceFile(String FileName, String oldBucketName, String newBucketName) throws FileNotFoundException {
        if (FileName.equals("notFoundFileName")) {
            throw new FileNotFoundException(String.format("file not found"));
        } else {
            log.info("Функция перемещения");
            return filesRepository.replaceFile(FileName, oldBucketName, newBucketName);
        }
    }

    public String deliteFile(String FileName) throws FileNotFoundException {
        if (FileName.equals("notFoundFileName")) {
            throw new FileNotFoundException(String.format("file not found"));
        } else {
            log.info("Функция удаления файла");
            return filesRepository.deliteFile(FileName);
        }
    }
    public String deliteBucket(String BucketName) throws BucketNotFoundException {
        if (BucketName.equals("notFoundBucketName")) {
            throw new BucketNotFoundException(String.format("bucket not found"));
        } else {
            log.info("Функция удаления бакета");
            return filesRepository.deliteBucket(BucketName);
        }
    }
}
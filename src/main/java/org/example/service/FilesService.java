package org.example.service;

import org.example.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FilesService {
    private static final Logger log = LoggerFactory.getLogger(FilesService.class);
    private final FilesRepository filesRepository;

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public String rename(String oldFileName, String newFileName) {
        log.info("Функция переименования");
        return filesRepository.rename(oldFileName, newFileName);
    }

    public String replace(String FileName, String oldBucketName, String newBucketName) {
        log.info("Функция перемещения");
        return filesRepository.replace(FileName, oldBucketName, newBucketName);
    }

    public String delite(String FileName) {
        log.info("Функция удаления");
        return filesRepository.delite(FileName);
    }
}
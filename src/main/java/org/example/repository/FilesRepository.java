package org.example.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class FilesRepository {
    private static final Logger log = LoggerFactory.getLogger(FilesRepository.class);

    public String rename(String oldFileName, String newFileName) {
        log.info("Функция переименования вызвана в репозитории");
        return "переименовываю файл " + oldFileName + " в " + newFileName;
    }
    public String replace(String FileName, String oldBucketName, String newBucketName) {
        log.info("Функция перемещения вызвана в репозитории");
        return "перемещаю файл " + FileName + " из " + oldBucketName + " в " + newBucketName;
    }
    public String delite(String FileName) {
        log.info("Функция удаления вызвана в репозитории");
        return "удаляю файл " + FileName;
    }
}
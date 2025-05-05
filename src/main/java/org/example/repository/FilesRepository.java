package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FilesRepository {

    public String renameFile(String oldFileName, String newFileName) {
        log.info("Функция переименования файла вызвана в репозитории");
        return "переименовываю файл " + oldFileName + " в " + newFileName;
    }

    public String renameBucket(String oldBucketName, String newBucketName) {
        log.info("Функция переименования бакета вызвана в репозитории");
        return "переименовываю бакет " + oldBucketName + " в " + newBucketName;
    }

    public String replaceFile(String FileName, String oldBucketName, String newBucketName) {
        log.info("Функция перемещения вызвана в репозитории");
        return "перемещаю файл " + FileName + " из " + oldBucketName + " в " + newBucketName;
    }

    public String deliteFile(String FileName) {
        log.info("Функция удаления файла вызвана в репозитории");
        return "удаляю файл " + FileName;
    }
    public String deliteBucket(String bucketName) {
        log.info("Функция удаления бакета вызвана в репозитории");
        return "удаляю бакет " + bucketName;
    }
}
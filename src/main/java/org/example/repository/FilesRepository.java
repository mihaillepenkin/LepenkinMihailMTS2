package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Slf4j
public class FilesRepository {
    private HashMap<String, String> files = new HashMap<>();
    private HashMap<String, ArrayList<String>> buckets = new HashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClient webClient = WebClient.create();

    public FilesRepository() {
        this.files.put("World War", "Book");
        this.files.put("War and Peace", "Book");
        this.files.put("Russia", "Country");
        this.files.put("USA", "Country");
        ArrayList<String> filesCountry = new ArrayList<>();
        ArrayList<String> filesBook = new ArrayList<>();
        filesBook.add("World War");
        filesBook.add("War and Peace");
        filesCountry.add("Russia");
        filesCountry.add("USA");
        this.buckets.put("Country", filesCountry);
        this.buckets.put("Book", filesBook);
    }

    public String renameFile(String oldFileName, String newFileName) throws FileNotFoundException {
        if (!files.containsKey(oldFileName)) {
            throw new FileNotFoundException(String.format("Файл %s не найден", oldFileName));
        }
        buckets.get(files.get(oldFileName)).remove(oldFileName);
        buckets.get(files.get(oldFileName)).add(newFileName);
        String bucketName = files.get(oldFileName);
        files.remove(oldFileName);
        files.put(newFileName, bucketName);
        log.info("Функция переименования файла вызвана в репозитории");
        return "переименовываю файл " + oldFileName + " в " + newFileName;
    }

    public String renameBucket(String oldBucketName, String newBucketName) throws BucketNotFoundException {
        if (!buckets.containsKey(oldBucketName)) {
            throw new BucketNotFoundException(String.format("Бакет %s не найден", oldBucketName));
        }
        ArrayList<String> helpArr = buckets.get(oldBucketName);
        buckets.remove(oldBucketName);
        buckets.put(newBucketName, helpArr);
        for (String el : helpArr) {
            files.remove(el);
            files.put(el, newBucketName);
        }
        log.info("Функция переименования бакета вызвана в репозитории");
        return "переименовываю бакет " + oldBucketName + " в " + newBucketName;
    }

    public void replaceFile(String oldBucketName, String FileName, String newBucketName) throws BucketNotFoundException, FileNotFoundException {
        if (!files.containsKey(FileName)) {
            throw new FileNotFoundException(String.format("Файл %s не найден", FileName));
        }
        if (!buckets.containsKey(oldBucketName)) {
            throw new BucketNotFoundException(String.format("Бакет %s не найден", oldBucketName));
        }
        if (!buckets.containsKey(newBucketName)) {
            throw new BucketNotFoundException(String.format("Бакет %s не найден", newBucketName));
        }
        buckets.get(oldBucketName).remove(FileName);
        buckets.get(newBucketName).add(FileName);
        files.remove(FileName);
        files.put(FileName, newBucketName);
        log.info("Функция перемещения вызвана в репозитории");
    }

    public String deleteFile(String FileName) throws FileNotFoundException {
        if (!files.containsKey(FileName)) {
            throw new FileNotFoundException(String.format("Файл %s не найден", FileName));
        }
        buckets.get(files.get(FileName)).remove(FileName);
        files.remove(FileName);
        log.info("Функция удаления файла вызвана в репозитории");
        String response = webClient.get().uri("http://localhost:5252/files/info/random-url").retrieve().bodyToMono(String.class).block();
        log.info(response);
        return "удаляю файл " + FileName;
    }
    public String deleteBucket(String bucketName) throws BucketNotFoundException {
        if (!buckets.containsKey(bucketName)) {
            throw new BucketNotFoundException(String.format("Бакет %s не найден", bucketName));
        }
        ArrayList<String> helpArr = buckets.get(bucketName);
        buckets.remove(bucketName);
        for (String el : helpArr) {
            files.remove(el);
        }
        log.info("Функция удаления бакета вызвана в репозитории");
        String response = restTemplate.getForObject("http://localhost:5252/buckets/info/random-url", String.class);
        log.info(response);
        return "удаляю бакет " + bucketName;
    }
}
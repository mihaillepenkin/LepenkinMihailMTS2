package org.example.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "apiCircuitBreaker")
public class FileControllerImplementationRate implements FileController{

    private final FileService filesService;

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    public FileControllerImplementationRate(FileService filesService) {
        this.filesService = filesService;
    }

    @Override
    @PatchMapping("/files/info/{oldFileName}/{newFileName}")
    public ResponseEntity<String> renameFile(@PathVariable("newFileName") String newFileName, @PathVariable("oldFileName") String oldFileName) throws FileNotFoundException, BucketNotFoundException {
        filesService.renameFile(oldFileName, newFileName);
        return ResponseEntity.ok("переименовываю файл " + oldFileName + " в " + newFileName);
    }
    @Override
    @PatchMapping("/files/info/{oldBucketName}/{fileName}/{newBucketName}")
    public ResponseEntity<String> replaceFile(@PathVariable("newBucketName") String newBucketName, @PathVariable("fileName") String FileName, @PathVariable("oldBucketName") String oldBucketName) throws FileNotFoundException, BucketNotFoundException {
        filesService.replaceFile(oldBucketName, FileName, newBucketName);
        return ResponseEntity.ok("перемещаю файл " + FileName + " из " + oldBucketName + " в " + newBucketName);
    }
    @Override
    @DeleteMapping("/files/info/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String FileName) throws FileNotFoundException, BucketNotFoundException {
        filesService.deleteFile(FileName);
        return ResponseEntity.ok("удаляю файл " + FileName);
    }
    @Override
    @PostMapping("/files/create/{bucketName}/{fileName}")
    public ResponseEntity<String> createFile(@PathVariable("fileName") String FileName, @PathVariable("bucketName") String BucketName) throws BucketNotFoundException {
        filesService.createFile(BucketName, FileName);
        return ResponseEntity.ok("создаю файл " + FileName);
    }
}
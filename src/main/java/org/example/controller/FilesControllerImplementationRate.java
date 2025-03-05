package org.example.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.service.FilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "apiCircuitBreaker")
public class FilesControllerImplementationRate implements FilesController{

    private final FilesService filesService;

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BucketNotFoundException.class)
    public ResponseEntity<String> handleBucketNotFoundException(BucketNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    public FilesControllerImplementationRate (FilesService filesService) {
        this.filesService = filesService;
    }

    @Override
    @PatchMapping("/files/info/{oldFileName}/{newFileName}")
    public ResponseEntity<String> renameFile(@PathVariable("newFileName") String newFileName, @PathVariable("oldFileName") String oldFileName) throws FileNotFoundException {
        return ResponseEntity.ok(filesService.renameFile(oldFileName, newFileName));
    }
    @Override
    @PatchMapping("/files/info/{oldBucketName}/{fileName}/{newBucketName}")
    public ResponseEntity<String> replaceFile(@PathVariable("newBucketName") String newBucketName, @PathVariable("fileName") String FileName, @PathVariable("oldBucketName") String oldBucketName) throws FileNotFoundException, BucketNotFoundException {
        filesService.replaceFile(oldBucketName, FileName, newBucketName);
        return ResponseEntity.ok("перемещаю файл " + FileName + " из " + oldBucketName + " в " + newBucketName);
    }
    @Override
    @DeleteMapping("/files/info/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String FileName) throws FileNotFoundException {
        return ResponseEntity.ok(filesService.deleteFile(FileName));
    }
    @Override
    @PatchMapping("/buckets/info/{oldBucketName}/{newBucketName}")
    public ResponseEntity<String> renameBucket(@PathVariable("newBucketName") String newBucketName, @PathVariable("oldBucketName") String oldBucketName) throws BucketNotFoundException {
        return ResponseEntity.ok(filesService.renameBucket(oldBucketName, newBucketName));
    }
    @Override
    @DeleteMapping("/buckets/info/{bucketName}")
    public ResponseEntity<String> deleteBucket(@PathVariable("bucketName") String bucketName) throws BucketNotFoundException {
        return ResponseEntity.ok(filesService.deleteBucket(bucketName));
    }
}
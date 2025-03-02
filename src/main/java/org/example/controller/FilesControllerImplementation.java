package org.example.controller;

import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.service.FilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FilesControllerImplementation implements FilesController{
    private final FilesService filesService;

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BucketNotFoundException.class)
    public ResponseEntity<String> handleBucketNotFoundException(BucketNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    public FilesControllerImplementation(FilesService filesService) {
        this.filesService = filesService;
    }
    @Override
    @GetMapping("/files/info/{oldFileName}/{newFileName}")
    public ResponseEntity<String> renameFile(@PathVariable("newFileName") String newFileName, @PathVariable("oldFileName") String oldFileName) throws FileNotFoundException {
        if (oldFileName.equals("notFoundOldFileName")) {
            throw new FileNotFoundException(String.format("file not found"));
        } else {
            return ResponseEntity.ok(filesService.renameFile(oldFileName, newFileName));
        }
    }
    @Override
    @GetMapping("/files/info/{oldBucketName}/{fileName}/{newBucketName}")
    public ResponseEntity<String> replaceFile(@PathVariable("newBucketName") String newBucketName, @PathVariable("fileName") String FileName, @PathVariable("oldBucketName") String oldBucketName) throws FileNotFoundException {
        if (FileName.equals("notFoundFileName")) {
            throw new FileNotFoundException(String.format("file not found"));
        } else {
            return ResponseEntity.ok(filesService.replaceFile(FileName, oldBucketName, newBucketName));
        }
    }
    @Override
    @GetMapping("/files/info/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String FileName) throws FileNotFoundException {
        if (FileName.equals("notFoundFileName")) {
            throw new FileNotFoundException(String.format("file not found"));
        } else {
            return ResponseEntity.ok(filesService.deliteFile(FileName));
        }
    }
    @Override
    @GetMapping("/buckets/info/{oldBucketName}/{newBucketName}")
    public ResponseEntity<String> renameBucket(@PathVariable("newBucketName") String newBucketName, @PathVariable("oldBucketName") String oldBucketName) throws BucketNotFoundException {
        if (oldBucketName.equals("notFoundOldBucketName")) {
            throw new BucketNotFoundException(String.format("bucket not found"));
        } else {
            return ResponseEntity.ok(filesService.renameBucket(oldBucketName, newBucketName));
        }
    }
    @Override
    @GetMapping("/buckets/info/{bucketName}")
    public ResponseEntity<String> deleteBucket(@PathVariable("bucketName") String bucketName) throws BucketNotFoundException {
        if (bucketName.equals("notFoundBucketName")) {
            throw new BucketNotFoundException(String.format("bucket not found"));
        } else {
            return ResponseEntity.ok(filesService.deliteBucket(bucketName));
        }
    }
}
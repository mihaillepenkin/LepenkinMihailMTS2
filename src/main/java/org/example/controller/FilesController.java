package org.example.controller;

import org.example.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FilesController {
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("/files/info/{oldFileName}/{newFileName}")
    public ResponseEntity<String> rename(@PathVariable("newFileName") String newFileName, @PathVariable("oldFileName") String oldFileName) {
        return ResponseEntity.ok(filesService.rename(oldFileName, newFileName));
    }

    @GetMapping("/files/info/{oldBucketName}/{fileName}/{newBucketName}")
    public ResponseEntity<String> replace(@PathVariable("newBucketName") String newBucketName, @PathVariable("fileName") String FileName, @PathVariable("oldBucketName") String oldBucketName) {
        return ResponseEntity.ok(filesService.replace(FileName, oldBucketName, newBucketName));
    }

    @GetMapping("/files/info/{fileName}")
    public ResponseEntity<String> delite(@PathVariable("fileName") String FileName) {
        return ResponseEntity.ok(filesService.delite(FileName));
    }
}
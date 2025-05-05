package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "FilesUtils API", description = "Files utils")

public interface FileController {
    @Operation(summary = "rename file")
    @ApiResponse(responseCode = "200", description = "file is renamed")
    @Parameter(in = ParameterIn.PATH, name = "oldFileName", description = "old file name", example = "2007")
    @Parameter(in = ParameterIn.PATH, name = "newFileName", description = "new file name", example = "2025")
    public ResponseEntity<String> renameFile(@PathVariable("newFileName") String newFileName, @PathVariable("oldFileName") String oldFileName) throws FileNotFoundException, BucketNotFoundException, JsonProcessingException;
    @Operation(summary = "replace file")
    @ApiResponse(responseCode = "200", description = "file is replaced")
    @Parameter(in = ParameterIn.PATH, name = "newBucketName", description = "new bucket name", example = "java")
    @Parameter(in = ParameterIn.PATH, name = "fileName", description = "file name", example = "MTS_Programmers")
    @Parameter(in = ParameterIn.PATH, name = "oldBucketName", description = "old bucket name", example = "assembler")
    public ResponseEntity<String> replaceFile(@PathVariable("newBucketName") String newBucketName, @PathVariable("fileName") String FileName, @PathVariable("oldBucketName") String oldBucketName) throws FileNotFoundException, BucketNotFoundException, JsonProcessingException;
    @Operation(summary = "delete file")
    @ApiResponse(responseCode = "200", description = "file is deleted")
    @Parameter(in = ParameterIn.PATH, name = "fileName", description = "file name", example = "Yandex")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String FileName) throws BucketNotFoundException, FileNotFoundException, JsonProcessingException;
    @Operation(summary = "create file")
    @ApiResponse(responseCode = "200", description = "file is created")
    @Parameter(in = ParameterIn.PATH, name = "fileName", description = "file name", example = "Yandex")
    @Parameter(in = ParameterIn.PATH, name = "bucketName", description = "bucket name", example = "Company")
    public ResponseEntity<String> createFile(@PathVariable("fileName") String FileName, @PathVariable("bucketName") String BucketName) throws BucketNotFoundException, FileNotFoundException, JsonProcessingException;
}

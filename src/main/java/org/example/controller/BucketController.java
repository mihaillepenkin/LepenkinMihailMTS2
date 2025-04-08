package org.example.controller;

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

public interface BucketController {
    @Operation(summary = "rename bucket")
    @ApiResponse(responseCode = "200", description = "bucket is renamed")
    @Parameter(in = ParameterIn.PATH, name = "oldBucketName", description = "old bucket name", example = "2007")
    @Parameter(in = ParameterIn.PATH, name = "newBucketName", description = "new bucket name", example = "2025")
    public ResponseEntity<String> renameBucket(@PathVariable("newBucketName") String newFileName, @PathVariable("oldBucketName") String oldFileName) throws FileNotFoundException, BucketNotFoundException;
    @Operation(summary = "delete bucket")
    @ApiResponse(responseCode = "200", description = "bucket is deleted")
    @Parameter(in = ParameterIn.PATH, name = "bucketName", description = "bucket name", example = "Yandex")
    public ResponseEntity<String> deleteBucket(@PathVariable("bucketName") String FileName) throws BucketNotFoundException;
    @Operation(summary = "create bucket")
    @ApiResponse(responseCode = "200", description = "bucket is created")
    @Parameter(in = ParameterIn.PATH, name = "bucketName", description = "bucket name", example = "Yandex")
    public ResponseEntity<String> createBucket(@PathVariable("bucketName") String bucketName) throws BucketNotFoundException;
}

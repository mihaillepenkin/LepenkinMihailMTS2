package org.example.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.SneakyThrows;
import org.example.exception.BucketNotFoundException;
import org.example.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RateLimiter(name = "rateLimiterAPI")
@CircuitBreaker(name = "apiCircuitBreaker")
public class BucketControllerImplementationRate implements BucketController{

    private final BucketService bucketService;


    @ExceptionHandler(BucketNotFoundException.class)
    public ResponseEntity<String> handleBucketNotFoundException(BucketNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    public BucketControllerImplementationRate(BucketService bucketService) {
        this.bucketService = bucketService;
    }


    @Override
    @PatchMapping("/buckets/info/{oldBucketName}/{newBucketName}")
    public ResponseEntity<String> renameBucket(@PathVariable("newBucketName") String newBucketName, @PathVariable("oldBucketName") String oldBucketName) throws BucketNotFoundException {
        bucketService.renameBucket(oldBucketName, newBucketName);
        return ResponseEntity.ok("переименовываю бакет " + oldBucketName + " в " + newBucketName);
    }
    @Override
    @DeleteMapping("/buckets/info/{bucketName}")
    public ResponseEntity<String> deleteBucket(@PathVariable("bucketName") String bucketName) throws BucketNotFoundException {
        bucketService.deleteBucket(bucketName);
        return ResponseEntity.ok("удалил бакет " + bucketName);
    }
    @Override
    @PostMapping("/buckets/create/{bucketName}")
    public ResponseEntity<String> createBucket(@PathVariable("bucketName") String bucketName) {
        bucketService.createBucket(bucketName);
        return ResponseEntity.ok("создал бакет " + bucketName);
    }
}
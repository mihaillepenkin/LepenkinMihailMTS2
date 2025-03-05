package org.example;

import io.minio.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@UtilityClass
@Slf4j

public final class S3FileUtils {

    private static final MinioClient client = Minio.getClient();

    public static void changeName(String bucketName, String oldKey, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newKey)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(oldKey)
                                        .build())
                        .build());
        deleteOne(bucketName, oldKey);
    }

    public static void copyInOtherPlace(String oldBucketName, String oldKey, String newBucketName, String newKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        client.copyObject(
                CopyObjectArgs.builder()
                        .bucket(newBucketName)
                        .object(newKey)
                        .source(
                                CopySource.builder()
                                        .bucket(oldBucketName)
                                        .object(oldKey)
                                        .build())
                        .build());
        deleteOne(oldBucketName, oldKey);
    }

    public static void deleteOne(String bucketName, String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        client.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(key).build());
    }
}


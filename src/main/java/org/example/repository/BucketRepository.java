package org.example.repository;

import org.example.entity.Bucket;
import org.example.exception.BucketNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByBucketNameEquals(String name) throws BucketNotFoundException;
    Optional<Bucket> findByBucketIdEquals(Long id) throws BucketNotFoundException;
}

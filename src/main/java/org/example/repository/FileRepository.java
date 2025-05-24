package org.example.repository;

import org.example.entity.File;
import java.util.Optional;
import org.example.exception.FileNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileNameEquals(String name) throws FileNotFoundException;
}
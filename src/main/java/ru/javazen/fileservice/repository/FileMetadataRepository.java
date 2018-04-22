package ru.javazen.fileservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.javazen.fileservice.model.File;

public interface FileMetadataRepository extends CrudRepository<File, Long> {

    File findByName(String name);
}

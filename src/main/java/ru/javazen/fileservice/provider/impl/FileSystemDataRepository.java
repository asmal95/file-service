package ru.javazen.fileservice.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javazen.fileservice.provider.FileDataRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemDataRepository implements FileDataRepository {

    private static Logger LOGGER = LoggerFactory.getLogger(FileSystemDataRepository.class);

    private String filesDirectory;

    public FileSystemDataRepository(String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }

    @Override
    public byte[] find(Long id) {
        Path path = resolvePath(id);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            LOGGER.error("Can't read the file {}", path, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Long id, byte[] bytes) {
        Path path = resolvePath(id);

        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            LOGGER.error("Can't add the file {}", path, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        Path path = resolvePath(id);

        try {
            Files.delete(path);
        } catch (IOException e) {
            LOGGER.error("Can't delete the file {}", path, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exist(Long id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    private Path resolvePath(Long id) {
        return Paths.get(filesDirectory, id.toString());
    }
}

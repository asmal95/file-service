package ru.javazen.fileservice.provider;

public interface FileDataRepository {

    byte[] find(Long id);

    void save(Long id, byte[] bytes);

    void delete(Long id);

    boolean exist(Long id);
}

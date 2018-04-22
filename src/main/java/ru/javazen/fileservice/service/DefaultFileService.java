package ru.javazen.fileservice.service;

import org.springframework.stereotype.Service;
import ru.javazen.fileservice.provider.FileDataRepository;
import ru.javazen.fileservice.model.File;
import ru.javazen.fileservice.repository.FileMetadataRepository;


@Service
public class DefaultFileService implements FileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final FileDataRepository fileDataRepository;

    public DefaultFileService(FileMetadataRepository fileMetadataRepository,
                              FileDataRepository fileProvider) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileDataRepository = fileProvider;
    }

    @Override
    public File findById(Long id) {
        File file = fileMetadataRepository.findOne(id);
        if (fileDataRepository.exist(id)) {
            file.setBytes(fileDataRepository.find(id));
        }
        return file;
    }

    @Override
    public File findMetadataById(Long id) {
        return fileMetadataRepository.findOne(id);
    }

    @Override
    public Long saveFile(File file) {
        File saved = fileMetadataRepository.save(file);
        if (file.getBytes() != null) {
            fileDataRepository.save(saved.getId(), file.getBytes());
        }
        return saved.getId();
    }

    @Override
    public void deleteFile(Long id) {
        fileDataRepository.delete(id);
        fileMetadataRepository.delete(id);
    }

    @Override
    public boolean exist(Long id) {
        return fileMetadataRepository.exists(id) && fileDataRepository.exist(id);
    }
}

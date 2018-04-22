package ru.javazen.fileservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.javazen.fileservice.service.FileService;
import ru.javazen.fileservice.model.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileServiceController {

    private final FileService fileService;
    private final String url;

    public FileServiceController(
            FileService fileService,
            @Value("${service.url}") String url) {
        this.fileService = fileService;
        this.url = url;
    }

    @RequestMapping(path = "/{id}/download", method = RequestMethod.GET)
    public ResponseEntity download(@PathVariable("id") Long id) {
        if (!fileService.exist(id)) {
            return ResponseEntity.notFound().build();
        }

        File file = fileService.findById(id);

        return ResponseEntity.ok()
                .contentLength(file.getSize())
                .contentType(MediaType.parseMediaType(file.getType()))
                .body(file.getBytes());
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Long> upload(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        fileMetadata.setSize(file.getSize());
        fileMetadata.setType(file.getContentType());
        fileMetadata.setDescription(description);

        fileMetadata.setBytes(file.getBytes());
        fileService.saveFile(fileMetadata);

        return ResponseEntity.ok(fileMetadata.getId());
    }

    @RequestMapping(path = "/{id}/info")
    public ResponseEntity info(@PathVariable("id") Long id) {
        if (!fileService.exist(id)) {
            return ResponseEntity.notFound().build();
        }
        File file = fileService.findMetadataById(id);
        Map<String, Object> res = new HashMap<>();
        res.put("id", file.getId());
        res.put("type", file.getType());
        res.put("size", file.getSize());
        res.put("name", file.getName());
        res.put("description", file.getDescription());
        res.put("url", url + "/file/" + file.getId() + "/download");

        return ResponseEntity.ok(res);
    }
}

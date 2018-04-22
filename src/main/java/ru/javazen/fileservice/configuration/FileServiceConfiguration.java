package ru.javazen.fileservice.configuration;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.javazen.fileservice.provider.FileDataRepository;
import ru.javazen.fileservice.provider.impl.FileSystemDataRepository;

@Configuration
public class FileServiceConfiguration implements EnvironmentAware {

    private String filesDirectory;

    @Bean
    public FileDataRepository fileProvider() {
        return new FileSystemDataRepository(filesDirectory);
    }

    @Override
    public void setEnvironment(Environment environment) {
        String filesDirectory = environment.getProperty("service.file.dir");
        if (filesDirectory == null) {
            filesDirectory = System.getProperty("user.home");
        }
        this.filesDirectory = filesDirectory;
    }
}

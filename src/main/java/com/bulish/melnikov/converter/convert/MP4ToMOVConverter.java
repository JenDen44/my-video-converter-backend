package com.bulish.melnikov.converter.convert;

import com.bulish.melnikov.converter.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MP4ToMOVConverter extends Mp4Converter {

    private final FileService fileService;
    public MP4ToMOVConverter(FileService fileService) {
        super("mov");
        this.fileService = fileService;
    }

    @Override
    public byte[] convert(File file) {

        try {
            Path newOutFileName = fileService.getNewPathForFile(
                    fileService.getFileNameWithoutExtension(file.getName()) + ".mov");

            String[] command = {
                    "ffmpeg",
                    "-i", file.getAbsolutePath(),
                    "-f", "mov",
                    newOutFileName.toString()
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            boolean finished = process.waitFor(2, TimeUnit.MINUTES);

            if (!finished) {
                process.destroy();
                log.error("FFmpeg process timed out");
                }

            try (FileInputStream fis = new FileInputStream(newOutFileName.toString())) {
                byte [] convertedVideo = fis.readAllBytes();

                fileService.deleteFile(newOutFileName.toString());

                return convertedVideo;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
}

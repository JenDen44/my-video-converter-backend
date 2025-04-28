package com.bulish.melnikov.converter.convert;

import com.bulish.melnikov.converter.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
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
    public byte[] convert(byte[] file) {
        File tempInputFile = null;
        Path tempOutputFile = null;

        try {
            tempInputFile = File.createTempFile("input-" + UUID.randomUUID(), ".mp4");
            tempOutputFile = Paths.get("temp/files/" + UUID.randomUUID() + ".mov");

            try(BufferedOutputStream buff = new BufferedOutputStream(Files.newOutputStream(tempInputFile.toPath()))) {
                buff.write(file);
                buff.flush();
            }

            String[] command = {
                    "ffmpeg",
                    "-i", tempInputFile.getAbsolutePath(),
                    "-f", "mov",
                    tempOutputFile.toString()
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            log.info("Convert process has started with command: {}", String.join(" ", command));

            log.info("convert process has started");
            Process process = processBuilder.start();
            boolean finished = process.waitFor(2, TimeUnit.MINUTES);

            if (!finished) {
                process.destroy();
                log.error("FFmpeg process timed out");
            } else {
                log.info("FFmpeg process finished successfully");
            }

            try (FileInputStream fis = new FileInputStream(tempOutputFile.toString())) {
                byte[] convertedVideo = fis.readAllBytes();

                fileService.deleteFile(tempInputFile.getPath(), tempOutputFile.toString());
                log.info("converted byte array size " + convertedVideo.length);

                return convertedVideo;
            }

        } catch (IOException exception) {
            log.debug("Error while create input/output files");
        } catch (InterruptedException ex) {
            log.debug("Error while process converting");
        }

        return null;
    }
}


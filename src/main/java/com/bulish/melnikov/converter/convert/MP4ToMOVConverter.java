package com.bulish.melnikov.converter.convert;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Component
public class MP4ToMOVConverter extends Mp4Converter {
    public MP4ToMOVConverter() {
        super("mov");
    }

    @Override
    public byte[] convert(File file) {

        try {
            File outputFile = File.createTempFile("output", ".mov");
            outputFile.deleteOnExit();

            String[] command = {
                    "ffmpeg",
                    "-i", file.getAbsolutePath(),
                    "-f", "mov",
                    outputFile.getAbsolutePath()
            };

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.redirectErrorStream(true); // Combine error stream with output stream
                Process process = processBuilder.start();

                boolean finished = process.waitFor(2, TimeUnit.MINUTES);

                if (!finished) {
                    process.destroy();
                    throw new RuntimeException("FFmpeg process timed out");
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("converting file was finished with code " + exitCode);
                }

            // Read the converted MOV file into a byte array
            try (FileInputStream fis = new FileInputStream(outputFile);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}

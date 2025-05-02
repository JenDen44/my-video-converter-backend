package com.bulish.melnikov.converter.convert;

import com.bulish.melnikov.converter.service.FFmpegService;
import com.bulish.melnikov.converter.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MP4ToMOVConverter extends Mp4Converter {

    private final FileService fileService;
    private final FFmpegService fFmpegService;

    public MP4ToMOVConverter(FileService fileService, FFmpegService fFmpegService) {
        super("mov");
        this.fileService = fileService;
        this.fFmpegService = fFmpegService;
    }

    @Override
    public byte[] convert(byte[] file) {
        return fFmpegService.convert(file, "mp4", "mov");
    }
}


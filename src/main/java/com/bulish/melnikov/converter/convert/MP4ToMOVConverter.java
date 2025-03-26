package com.bulish.melnikov.converter.convert;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MP4ToMOVConverter extends Mp4Converter {
    public MP4ToMOVConverter() {
        super("mov");
    }

    @Override
    public byte[] convert(File file) {
       return null;
    }
}

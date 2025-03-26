package com.bulish.melnikov.converter.fabric;

import com.bulish.melnikov.converter.convert.Mp4Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MP4Fabric extends Fabric {

    @Autowired
    public MP4Fabric(List<Mp4Converter> converters) {
        super("mp4");

        for (Mp4Converter converter : converters) {
            this.converters.put(converter.getFormat(), converter);
        }
    }
}

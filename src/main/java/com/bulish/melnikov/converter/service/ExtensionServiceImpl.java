package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ExtensionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtensionServiceImpl implements ExtensionService {

    private static final List<ExtensionDto> extensions;

    static {
        extensions = List.of(
                new ExtensionDto("mp4", List.of("mov"), 10));
    }
    @Override
    public List<ExtensionDto> getAllowedExtensions() {
        return extensions;
    }
}

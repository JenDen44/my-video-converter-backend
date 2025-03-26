package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ConverterFileService {
    ConvertResponse requestToConvert(MultipartFile file, String toFormat) throws IOException;

    ConvertResponse getRequestStatusById(String id);

    byte[] downloadConvertedFile(String id);
}

package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.convert.Converter;
import com.bulish.melnikov.converter.fabric.ConverterFactory;
import com.bulish.melnikov.converter.fabric.Fabric;
import com.bulish.melnikov.converter.model.ConvertRequest;
import com.bulish.melnikov.converter.model.State;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class ConverterServiceImpl implements ConverterService {

    private final FileService fileService;
    private final ConverterFactory converterFactory;
    private final ConvertRequestService convertRequestService;
    @Override
    public void convert(ConvertRequest request) {
        request.setState(State.CONVERTING);
        convertRequestService.update(request);

        Fabric fabric = converterFactory.getFactory(request.getFormatFrom());
        Converter converter = fabric.getConverter(request.getFormatTo());

        File fileToConvert = fileService.getFile(request.getFilePath());
        String convertedFilePath = null;

        try {
            byte[] convertedFile = converter.convert(fileToConvert);
            convertedFilePath = fileService.saveFile(convertedFile, request.getFormatTo(), fileToConvert.getName());
        } catch (IOException e) {
            request.setState(State.IN_ERROR);
            convertRequestService.save(request);
            throw new RuntimeException("Error converting file", e);
        }

        request.setConvertedFilePath(convertedFilePath);
        request.setState(State.CONVERTED);
        convertRequestService.update(request);
    }
}

package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.convert.Converter;
import com.bulish.melnikov.converter.exception.IncorrectFormatExtensionException;
import com.bulish.melnikov.converter.fabric.ConverterFactory;
import com.bulish.melnikov.converter.fabric.Fabric;
import com.bulish.melnikov.converter.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConverterServiceImpl implements ConverterService {

    private final ConverterFactory converterFactory;
    private final ExtensionService extensionService;
    private final StreamBridge streamBridge;

    @Value("${convert.video.response.destination}")
    private String destination;

    @Override
    public void convert(ConvertRequestMsgDTO request) {

        List<ExtensionDto> extensions = extensionService.getAllowedExtensions();
        String formatTo = request.getFormatTo();
        String formatFrom = request.getFormatFrom();

        if (extensions.stream().noneMatch(e -> e.getFormatFrom().equals(formatFrom)
                && e.getFormatsTo().contains(formatTo))) {
            throw new IncorrectFormatExtensionException("Check available formats, format from "
                    + formatFrom + " to  " +  formatTo + " is not supported");
        }

        Fabric fabric = converterFactory.getFactory(request.getFormatFrom());
        Converter converter = fabric.getConverter(request.getFormatTo());

        byte[] convertedFile;

        try {
            //TODO send even that status for request status is converting
            convertedFile = converter.convert(request.getFile());

         if (convertedFile.length != 0) {
             //TODO sent event file is converted
             streamBridge.send(destination, ConvertResponseMsgDTO.builder()
                     .file(convertedFile)
                     .formatFrom(formatFrom)
                     .formatTo(formatTo)
                     .build());
         } else {
             throw new RuntimeException("converted file array is empty");
         }

        } catch (Exception e) {
           //TODO send even status Request is in error
            throw new RuntimeException("Error converting file", e);
        }
    }
}

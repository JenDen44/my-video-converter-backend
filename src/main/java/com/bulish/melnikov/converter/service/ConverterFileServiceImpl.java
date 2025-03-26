package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.exception.IncorrectFormatExtensionException;
import com.bulish.melnikov.converter.mapper.ConvertRequestToConvertResponseMapper;
import com.bulish.melnikov.converter.model.ConvertRequest;
import com.bulish.melnikov.converter.model.ConvertResponse;
import com.bulish.melnikov.converter.model.ExtensionDto;
import com.bulish.melnikov.converter.model.State;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ConverterFileServiceImpl implements ConverterFileService {

    private final ConvertRequestService convertRequestService;
    private final ConverterRequestQueueManagerServiceImpl queueService;
    private final ConvertRequestToConvertResponseMapper requestToConvertResponseMapper;
    private final FileService fileService;
    private final ExtensionService extensionService;

    private final String dirUpload = "temp/files/";

    @Override
    public ConvertResponse requestToConvert(MultipartFile fileToConvert, String formatTo) {
        String formatFrom = fileService.getFileExtension(fileToConvert.getOriginalFilename());

        List<ExtensionDto> extensions = extensionService.getAllowedExtensions();
        if (!extensions.stream().anyMatch(e -> e.getFormatFrom().equals(formatFrom)
                && e.getFormatsTo().contains(formatTo))) {

            throw new IncorrectFormatExtensionException("Check available formats formatTo "
                    + formatTo + " or formatFrom " +  formatFrom + "are not supported");
        }

        String filePath = null;
        try {
            filePath = fileService.saveMultipartFile(fileToConvert);
        } catch (IOException e) {
            log.error("Error uploading Multipart File", e);
            throw new RuntimeException(e);
        }

        ConvertRequest convertRequest = new ConvertRequest(filePath, formatTo, formatFrom);
        convertRequestService.save(convertRequest);

        queueService.addRequestToQueue(convertRequest);

        return requestToConvertResponseMapper.convertRequestToConvertResponse(convertRequest);
    }

    @Override
    public ConvertResponse getRequestStatusById(String id) {
       ConvertRequest convertRequest = convertRequestService.get(id);

        return requestToConvertResponseMapper.convertRequestToConvertResponse(convertRequest);
    }

    @Override
    public byte[] downloadConvertedFile(String id) {
        ConvertRequest convertRequest = convertRequestService.get(id);

        if (convertRequest.getState() == State.CONVERTED) {
            try {
                return Files.readAllBytes(Paths.get(convertRequest.getConvertedFilePath()));
            } catch (IOException e) {
                throw new RuntimeException("Error downloading file", e);
            }
        } else {
            log.error("File status " + convertRequest.getState() + " is not converted");
            throw new RuntimeException("File status " + convertRequest.getState() + " is not converted");
        }
    }
}

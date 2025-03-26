package com.bulish.melnikov.converter.controller;

import com.bulish.melnikov.converter.model.ConvertResponse;
import com.bulish.melnikov.converter.model.ExtensionDto;
import com.bulish.melnikov.converter.service.ConverterFileService;
import com.bulish.melnikov.converter.service.ExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConverterVideoController {

    private final ConverterFileService converterFileService;
    private final ExtensionService extensionService;

    @PostMapping("/convert")
    @ResponseStatus(HttpStatus.OK)
    public ConvertResponse convert(@RequestParam("file") MultipartFile file,
                                   @RequestParam("toFormat") String toFormat) throws IOException {
        return converterFileService.requestToConvert(file, toFormat);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConvertResponse requestStatus(@PathVariable("id") String id) {
        return converterFileService.getRequestStatusById(id);
    }

    @GetMapping("/{id}/download")
    @ResponseStatus(HttpStatus.OK)
    public byte[] download(@PathVariable("id") String id) {
        return converterFileService.downloadConvertedFile(id);
    }

    @GetMapping("/extensions")
    @ResponseStatus(HttpStatus.OK)
    public List<ExtensionDto> allowedExtensions() {
        return extensionService.getAllowedExtensions();
    }
}

package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertRequest;
import com.bulish.melnikov.converter.model.State;
import com.bulish.melnikov.converter.repository.ConverterRequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ConvertRequestServiceImpl implements ConvertRequestService {

    private final ConverterRequestRepository requestRepo;
    private final FileService fileService;

    @Override
    public ConvertRequest save(ConvertRequest convertRequest) {
        return requestRepo.save(convertRequest);
    }

    @Override
    public ConvertRequest get(String convertRequestId) {
        return requestRepo.findById(convertRequestId).orElseThrow(()
                -> new RuntimeException("File not found"));
    }

    @Override
    public ConvertRequest update(ConvertRequest convertRequest) {
        ConvertRequest convertRequestFromRedis = get(convertRequest.getId());

        convertRequestFromRedis.setState(convertRequest.getState());
        convertRequestFromRedis.setConvertedFilePath(convertRequest.getConvertedFilePath());

        requestRepo.save(convertRequestFromRedis);

        return convertRequestFromRedis;
    }

    @Override
    public void delete(String convertRequestId) {
        requestRepo.deleteById(convertRequestId);
    }

    public List<ConvertRequest> getOldConvertRequests() {
        LocalDateTime dateTime = LocalDateTime.now().minusHours(2);
        List<ConvertRequest> convertRequests = new ArrayList<>();

        Iterable<ConvertRequest> requests = requestRepo.findAll();

        for (ConvertRequest request : requests) {

            if ((request.getState() == State.CONVERTED ||
                    request.getState() == State.IN_ERROR) &&
                    request.getConversionDate().isBefore(dateTime)) {

                convertRequests.add(request);
            }
        }

        return convertRequests;
    }

    public void deleteOldConvertRequestWithFiles() {
        List<ConvertRequest> convertRequests = getOldConvertRequests();

        for (ConvertRequest request : convertRequests) {
            try {
                fileService.deleteFile(request.getFilePath(), request.getConvertedFilePath());
            } catch (IOException e) {
                convertRequests.remove(request);
                log.error("Error while removing the file from local system");
            }
        }
        if (!convertRequests.isEmpty()) requestRepo.deleteAll(convertRequests);
    }
}

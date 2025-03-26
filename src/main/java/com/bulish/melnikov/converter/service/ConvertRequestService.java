package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertRequest;

public interface ConvertRequestService {

    ConvertRequest save(ConvertRequest convertRequest);

    ConvertRequest get(String convertRequestId);

    ConvertRequest update(ConvertRequest convertRequest);

    void delete(String convertRequestId);

    void deleteOldConvertRequestWithFiles();
}

package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertRequest;

public interface ConverterRequestQueueManagerService {

    void addRequestToQueue(ConvertRequest convertRequest);
}

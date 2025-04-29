package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertRequestMsgDTO;

public interface ConverterRequestQueueManagerService {

    void addRequestToQueue(ConvertRequestMsgDTO convertRequest);
}

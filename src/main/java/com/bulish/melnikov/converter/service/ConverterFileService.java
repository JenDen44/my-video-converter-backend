package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertRequestMsgDTO;

public interface ConverterFileService {
    void requestToConvert(ConvertRequestMsgDTO convertRequest);
}

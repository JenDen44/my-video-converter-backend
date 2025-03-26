package com.bulish.melnikov.converter.mapper;

import com.bulish.melnikov.converter.model.ConvertRequest;
import com.bulish.melnikov.converter.model.ConvertResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConvertRequestToConvertResponseMapper {

    ConvertResponse convertRequestToConvertResponse(ConvertRequest request);
}

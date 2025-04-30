package com.bulish.melnikov.converter.functions;

import com.bulish.melnikov.converter.model.ConvertRequestMsgDTO;
import com.bulish.melnikov.converter.service.ConverterRequestQueueManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConverterVideoFunction {

    private final ConverterRequestQueueManagerService queueService;

    @Bean
    public Consumer<ConvertRequestMsgDTO> convert() {
        return convertRequest -> {
            log.info("Request to convert arrived : from " +  convertRequest.getFormatFrom()
                    + " to " + convertRequest.getFormatTo());
            queueService.addRequestToQueue(convertRequest);
        };
    }
}

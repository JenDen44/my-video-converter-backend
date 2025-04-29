package com.bulish.melnikov.converter.service;

import com.bulish.melnikov.converter.model.ConvertRequestMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@Slf4j
public class ConverterRequestQueueManagerServiceImpl implements ConverterRequestQueueManagerService {

    private final BlockingQueue<ConvertRequestMsgDTO> taskQueue = new LinkedBlockingQueue<>();

    private final ConverterService converterService;

    private final ExecutorService executorService;
    private volatile boolean running = true;

    public ConverterRequestQueueManagerServiceImpl(ConverterService converterService) {
        this.converterService = converterService;
        this.executorService = Executors.newFixedThreadPool(4);
        new Thread(this::processQueue).start();
    }

    public void addRequestToQueue(ConvertRequestMsgDTO convertRequest) {
        taskQueue.add(convertRequest);
    }

    private void processQueue() {
        while (running) {
            try {
                ConvertRequestMsgDTO request = taskQueue.take();
                executorService.submit(() -> {
                    try {
                        converterService.convert(request);
                    } catch (Exception e) {
                        log.error("Error converting file", e);
                    }
                });

            } catch (InterruptedException e) {
                log.warn("Queue processing was interrupted, but will continue processing pending requests.", e);
                Thread.currentThread().interrupt();
                shutdown();
            }
        }
    }

    public void shutdown() {
        running = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

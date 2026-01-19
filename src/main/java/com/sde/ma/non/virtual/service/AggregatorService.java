package com.sde.ma.non.virtual.service;

import com.sde.ma.non.virtual.model.ResponseModel;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Service
public class AggregatorService {

    public ResponseModel aggregate(int delay, int fanOut, int payload) throws InterruptedException {

        int poolSize = Math.min(
                fanOut,
                Runtime.getRuntime().availableProcessors()
        );

        try (ExecutorService executor = Executors.newFixedThreadPool(poolSize)) {

            long startNs = System.nanoTime();

            List<Future<Integer>> futures = IntStream.range(0, fanOut)
                    .mapToObj(id -> executor.submit(() -> work(id, delay, payload)))
                    .toList();

            int totalBytes = 0;
            int success = 0;

            for (Future<Integer> f : futures) {
                    totalBytes += f.get();
                    success++;
            }

            long durationMs =
                    Duration.ofNanos(System.nanoTime() - startNs).toMillis();

            return ResponseModel.builder()
                    .fanOut(fanOut)
                    .success(success)
                    .payloadBytes(totalBytes)
                    .durationMs(durationMs)
                    .build();

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException(e.getMessage());
        }
    }

    private int work(int taskId, int delayMs, int payloadBytes) throws InterruptedException {

        int effectiveDelayMs = Math.max(0, delayMs);
        int effectivePayloadBytes = Math.max(0, payloadBytes);

        if (effectiveDelayMs > 0) {
            Thread.sleep(effectiveDelayMs);
        }

        final int WORK_UNITS_PER_BYTE = 50;
        long totalIterations = (long) effectivePayloadBytes * WORK_UNITS_PER_BYTE;

        totalIterations = Math.min(totalIterations, 200_000_000L);

        long mixState = 0x9E3779B97F4A7C15L ^ taskId;
        mixState ^= (long) effectiveDelayMs << 32;
        mixState ^= effectivePayloadBytes;

        for (long iteration = 0; iteration < totalIterations; iteration++) {
            mixState ^= (mixState << 13);
            mixState ^= (mixState >>> 7);
            mixState ^= (mixState << 17);

            mixState += iteration;
        }

        return effectivePayloadBytes + (int) (mixState & 0xFF);
    }
}

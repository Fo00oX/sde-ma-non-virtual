package com.sde.ma.non.virtual.service;

import com.sde.ma.non.virtual.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
@Service
public class AggregatorService {

    @Value("${downstream.base-url}")
    private String downstreamBaseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public ResponseModel aggregate(int delay, int fanOut, int payload) throws InterruptedException {

        int poolSize = Math.min(
                fanOut,
                Runtime.getRuntime().availableProcessors()
        );

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        long startNs = System.nanoTime();

        try {
            List<Future<Integer>> futures = IntStream
                    .range(0, fanOut)
                    .mapToObj(id ->
                            executor.submit(() -> callDownstream(id, delay, payload))
                    )
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
        } finally {
            executor.shutdown();
        }
    }

    private int callDownstream(int id, int delay, int payload) throws Exception {

        Thread t = Thread.currentThread();
        log.info(
                "Downstream call id={} on thread={} (id={})",
                id, t.getName(), t.getId()
        );

        String url = String.format(
                "%s/api/downstream/%d?delay=%d&payload=%d",
                downstreamBaseUrl, id, delay, payload
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        HttpResponse<byte[]> response =
                client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        return response.body().length;
    }
}

package com.sde.ma.non.virtual.service;

import com.sde.ma.non.virtual.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregatorService {

    @Value("${downstream.base-url}")
    private String downstreamBaseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public ResponseModel request(int delay, int fanOut, int payload) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(
                Math.min(fanOut, Runtime.getRuntime().availableProcessors())
        );

        List<Future<Integer>> futures = new ArrayList<>(fanOut);

        long start = System.nanoTime();

        try {
            for (int i = 0; i < fanOut; i++) {
                final int id = i;

                futures.add(executor.submit(() -> callDownstream(id, delay, payload)));
            }

            int totalBytes = 0;
            int success = 0;

            for (Future<Integer> future : futures) {
                try {
                    totalBytes += future.get();
                    success++;
                } catch (ExecutionException e) {
                    log.warn("Downstream call failed", e.getCause());
                }
            }

            long durationMs = Duration.ofNanos(System.nanoTime() - start).toMillis();

            return ResponseModel.builder()
                    .fanOut(fanOut)
                    .success(success)
                    .payloadBytes(totalBytes)
                    .durationMs(durationMs)
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private int callDownstream(int id, int delay, int payload) throws HttpClientErrorException, IOException, InterruptedException {

        String url = String.format(
                "%s/api/downstream/%d?delayMs=%d&payloadBytes=%d",
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
package com.xuan.add2num.web.service;

import com.xuan.add2num.MyBigNumber;
import com.xuan.add2num.web.progress.CalculationJob;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Add2NumService {

    private final MyBigNumber add2Num = new MyBigNumber();

    private final ExecutorService executor =
            Executors.newCachedThreadPool();

    private final ConcurrentHashMap<String, CalculationJob> jobs =
            new ConcurrentHashMap<>();

    public String startCalculation(
            String stn1,
            String stn2) {

        String jobId = UUID.randomUUID().toString();

        CalculationJob job = new CalculationJob();

        jobs.put(jobId, job);

        executor.submit(() -> {

            try {

                String result = add2Num.sum(
                        stn1,
                        stn2,
                        progress -> {

                            job.setProgress(progress);

                            sendProgress(job, progress);
                        }
                );

                job.setResult(result);
                job.setProgress(100);
                job.setCompleted(true);

                sendResult(job, result);

            } catch (Exception e) {

                job.setError(e.getMessage());
                job.setCompleted(true);

                sendError(job, e.getMessage());
            }
        });

        return jobId;
    }

    public void registerEmitter(
            String jobId,
            SseEmitter emitter) {

        CalculationJob job = jobs.get(jobId);

        if (job == null) {

            try {
                emitter.send(
                        SseEmitter.event()
                                .name("error")
                                .data("Job not found")
                );

                emitter.complete();

            } catch (IOException e) {
                emitter.completeWithError(e);
            }

            return;
        }

        job.setEmitter(emitter);

        /*
         * Nếu calculation đã chạy xong trước khi browser
         * kết nối SSE thì vẫn gửi trạng thái cuối.
         */
        try {

            emitter.send(
                    SseEmitter.event()
                            .name("progress")
                            .data(job.getProgress())
            );

            if (job.isCompleted()) {

                if (job.getError() != null) {

                    sendError(job, job.getError());

                } else {

                    sendResult(job, job.getResult());
                }
            }

        } catch (IOException e) {

            emitter.completeWithError(e);
        }
    }

    private void sendProgress(
            CalculationJob job,
            int progress) {

        SseEmitter emitter = job.getEmitter();

        if (emitter == null) {
            return;
        }

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("progress")
                            .data(progress)
            );

        } catch (IOException e) {

            emitter.completeWithError(e);
        }
    }

    private void sendResult(
            CalculationJob job,
            String result) {

        SseEmitter emitter = job.getEmitter();

        if (emitter == null) {
            return;
        }

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("result")
                            .data(result)
            );

            emitter.complete();

        } catch (IOException e) {

            emitter.completeWithError(e);
        }
    }

    private void sendError(
            CalculationJob job,
            String message) {

        SseEmitter emitter = job.getEmitter();

        if (emitter == null) {
            return;
        }

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("error")
                            .data(message)
            );

            emitter.complete();

        } catch (IOException e) {

            emitter.completeWithError(e);
        }
    }

    public CalculationJob getJob(String jobId) {
        return jobs.get(jobId);
    }
}
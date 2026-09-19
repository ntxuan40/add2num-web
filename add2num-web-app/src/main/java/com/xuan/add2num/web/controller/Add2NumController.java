package com.xuan.add2num.web.controller;


import com.xuan.add2num.web.service.Add2NumService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
public class Add2NumController {

    private final Add2NumService service;

    public Add2NumController(Add2NumService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public StartResponse add(
            @RequestParam String stn1,
            @RequestParam String stn2) {

        String jobId =
                service.startCalculation(stn1, stn2);

        return new StartResponse(jobId);
    }

    @GetMapping(
            value = "/progress/{jobId}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter progress(
            @PathVariable String jobId) {

        /*
         * Timeout 0 = không timeout.
         */
        SseEmitter emitter =
                new SseEmitter(0L);

        service.registerEmitter(
                jobId,
                emitter
        );

        return emitter;
    }

    public record StartResponse(String jobId) {
    }
}
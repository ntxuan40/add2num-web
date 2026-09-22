package com.xuan.add2num.web.progress;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class CalculationJob {

    private volatile int progress;
    private volatile boolean completed;
    private volatile String result;
    private volatile String error;

    private SseEmitter emitter;

    public CalculationJob() {
        this.progress = 0;
        this.completed = false;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }

    public void setEmitter(SseEmitter emitter) {
        this.emitter = emitter;
    }
}
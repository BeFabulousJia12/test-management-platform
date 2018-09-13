package com.test.collectionService.TestPlatformServer.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * @Author You Jia
 * @Date 7/25/2018 1:11 PM
 */
public class TestResultSummary {
    String testName;
    int passed;
    int failed;
    int ignored;
    long duration;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    Timestamp timestamp;
    String user;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getFailed() {
        return failed;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getIgnored() {
        return ignored;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getPassed() {
        return passed;
    }

    public void setIgnored(int ignored) {
        this.ignored = ignored;
    }

    public long getDuration() {
        return duration;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}

package com.test.collectionService.TestPlatformServer.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @Author You Jia
 * @Date 8/9/2018 2:25 PM
 */
public class AccountInfo {
    @NotNull
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    public Timestamp timestamp;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

package com.github.salvatorenovelli.migrationbuddy.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlStats {
    private String url;
    private long sessions;
    private long newUsers;
}

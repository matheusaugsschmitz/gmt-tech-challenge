package com.gmt.gmttechchallenge.client.youtube;

import java.time.LocalDateTime;

/*
    This emulates a payload structure that YouTube current API would return,
    but only with the data that will be used by this project.
 */
public record YouTubeVideoDto(String id, Snippet snippet, Statistics statistics, FileDetails fileDetails) {

    public record Snippet(LocalDateTime publishedAt, String channelTitle, String[] tags){}
    public record Statistics(String viewCount){}
    public record FileDetails(Long durationMs){}
}

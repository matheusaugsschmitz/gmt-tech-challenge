package com.gmt.gmttechchallenge.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

public record VideoMetadata (Integer id, String sourceId, VideoSource source,  LocalDateTime publishedAt, String[] tags,  BigInteger viewCount, String channelTitle,  Long durationMs) {

    public static VideoMetadata create(String sourceId, VideoSource source, LocalDateTime publishedAt, String[] tags, BigInteger viewCount, String channelTitle, Long durationMs){
        int id = generateId(sourceId, source);
        return new VideoMetadata(id, sourceId, source, publishedAt, tags, viewCount, channelTitle, durationMs);
    }

    public static int generateId(String sourceId, VideoSource source) {
        return Objects.hash(sourceId, source.toString());
    }

}

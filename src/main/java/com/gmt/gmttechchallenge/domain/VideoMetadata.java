package com.gmt.gmttechchallenge.domain;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

public record VideoMetadata (UUID id, String sourceId, VideoSource source, LocalDateTime publishedAt, String[] tags, BigInteger viewCount, String channelTitle, Long durationMs) {

    public static VideoMetadata create(String sourceId, VideoSource source, LocalDateTime publishedAt, String[] tags, BigInteger viewCount, String channelTitle, Long durationMs){
        UUID id = generateId(sourceId, source);
        return new VideoMetadata(id, sourceId, source, publishedAt, tags, viewCount, channelTitle, durationMs);
    }

    public static UUID generateId(String sourceId, VideoSource source) {
        var combinedString = source.toString() + ":" + sourceId;
        return UUID.nameUUIDFromBytes(combinedString.getBytes(StandardCharsets.UTF_8));
    }

}

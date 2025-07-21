package com.gmt.gmttechchallenge.domain;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class VideoMetadata {
    String id; // TODO: improve to avoid id conflict cross-source
    VideoSource source;
    LocalDateTime publishedAt;
    String[] tags;
    BigInteger viewCount;
    String channelTitle;
    Long durationMs;
}

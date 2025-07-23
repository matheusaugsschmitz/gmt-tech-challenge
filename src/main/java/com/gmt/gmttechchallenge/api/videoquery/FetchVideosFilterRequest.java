package com.gmt.gmttechchallenge.api.videoquery;

import com.gmt.gmttechchallenge.domain.VideoSource;

import java.time.LocalDate;

public record FetchVideosFilterRequest(VideoSource source, String sourceId, LocalDate uploadDate, Long minDurationMs, Long maxDurationMs) {
}

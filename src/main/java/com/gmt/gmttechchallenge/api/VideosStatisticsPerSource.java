package com.gmt.gmttechchallenge.api;

import com.gmt.gmttechchallenge.domain.VideoSource;

public record VideosStatisticsPerSource(VideoSource source, int totalVideosImported, double averageDuration) {
}

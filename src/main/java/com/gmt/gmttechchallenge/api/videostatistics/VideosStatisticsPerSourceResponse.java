package com.gmt.gmttechchallenge.api.videostatistics;

import com.gmt.gmttechchallenge.domain.VideoSource;

public record VideosStatisticsPerSourceResponse(VideoSource source, int totalVideosImported, double averageDuration) {
}

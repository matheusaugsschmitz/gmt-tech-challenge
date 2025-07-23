package com.gmt.gmttechchallenge.api.videoimport;

import com.gmt.gmttechchallenge.domain.VideoSource;

import java.util.List;

public record VideosImportRequest(List<VideoIdentifier> videoIdentifiers) {

    public record VideoIdentifier(String videoId, VideoSource source) {
    }
}

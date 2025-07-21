package com.gmt.gmttechchallenge.api;

import com.gmt.gmttechchallenge.domain.VideoSource;

import java.util.List;

public record VideosImport(List<VideoIdentifier> videoIdentifiers) {

    public record VideoIdentifier(String videoId, VideoSource source) {
    }
}

package com.gmt.gmttechchallenge.api.videoimport;

import com.gmt.gmttechchallenge.domain.VideoSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record VideosImportRequest(@Size(min = 1) List<VideoIdentifier> videoIdentifiers) {

    public record VideoIdentifier(@NotBlank String videoId, @NotNull VideoSource source) {
    }
}

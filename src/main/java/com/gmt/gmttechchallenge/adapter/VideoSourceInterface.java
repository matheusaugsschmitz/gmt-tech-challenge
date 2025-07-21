package com.gmt.gmttechchallenge.adapter;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;

import java.util.List;

public interface VideoSourceInterface {

    VideoSource getVideoSource();

    List<VideoMetadata> importBatchMetadata(List<String> ids);
}

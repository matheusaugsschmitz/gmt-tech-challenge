package com.gmt.gmttechchallenge.client.vimeo;

import java.time.LocalDateTime;

/*
    This emulates a payload structure that Vimeo current API would return,
    but only with the data that will be used by this project.

    PS. Vimeo API isn't this nice, using the real API would require multiple API calls to get all data, so for
    convenience for this test I'm pretending it returns everything we need right away in a single-layered object.
 */
public record VimeoVideoDto(Long id, LocalDateTime createdTime, Long duration, String[] tags, Long viewCount, String channelTitle) {

}

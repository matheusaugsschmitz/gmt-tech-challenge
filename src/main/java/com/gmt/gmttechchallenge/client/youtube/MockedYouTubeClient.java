package com.gmt.gmttechchallenge.client.youtube;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class MockedYouTubeClient {

    private static final Map<String, YouTubeVideoDto> videos = Map.of(
            "abc123", new YouTubeVideoDto(
                    "abc123",
                    new YouTubeVideoDto.Snippet(LocalDateTime.now(), "channel abc", new String[]{"n8n", "k8s", "cluster", "proxmox", "homelab"}),
                    new YouTubeVideoDto.Statistics("32478240"),
                    new YouTubeVideoDto.FileDetails(54165163654L)
            ),
            "def456", new YouTubeVideoDto(
                    "def456",
                    new YouTubeVideoDto.Snippet(LocalDateTime.now(), "channel def4", new String[]{"nintendo", "switch2", "soldout"}),
                    new YouTubeVideoDto.Statistics("2715092949"),
                    new YouTubeVideoDto.FileDetails(54165163654L)
            ),
            "ghi789", new YouTubeVideoDto(
                    "ghi789",
                    new YouTubeVideoDto.Snippet(LocalDateTime.now(), "channel ghi", new String[]{"openai", "chatgpt", "agent", "operator"}),
                    new YouTubeVideoDto.Statistics("71229"),
                    new YouTubeVideoDto.FileDetails(54165163654L)
            ),
            "jkl123", new YouTubeVideoDto(
                    "jkl123",
                    new YouTubeVideoDto.Snippet(LocalDateTime.now(), "channel jkl", new String[]{"recovery", "wrecker", "offroad"}),
                    new YouTubeVideoDto.Statistics("469946885"),
                    new YouTubeVideoDto.FileDetails(54165163654L)
            )
    );

    public List<YouTubeVideoDto> fetchVideosByIdList(List<String> ids) {
        return ids.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(id -> !id.isBlank())
                .distinct()
                .map(videos::get)
                .collect(toList());
    }
}
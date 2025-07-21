package com.gmt.gmttechchallenge.client.vimeo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class MockedVimeoClient {

    private static final Map<Long, VimeoVideoDto> videos = Map.of(
            123456L, new VimeoVideoDto(
                    123456L,
                    LocalDateTime.now(),
                    54165163654L,
                    new String[]{"n8n", "k8s", "cluster", "proxmox", "homelab"},
                    32478240L,
                    "channel abc"
            ),
            234567L, new VimeoVideoDto(
                    234567L,
                    LocalDateTime.now(),
                    54165163654L,
                    new String[]{"nintendo", "switch2", "soldout"},
                    2715092949L,
                    "channel def"
            ),
            345678L, new VimeoVideoDto(
                    345678L,
                    LocalDateTime.now(),
                    54165163654L,
                    new String[]{"openai", "chatgpt", "agent", "operator"},
                    71229L,
                    "channel ghi"
            ),
            456789L, new VimeoVideoDto(
                    456789L,
                    LocalDateTime.now(),
                    54165163654L,
                    new String[]{"recovery", "wrecker", "offroad"},
                    469946885L,
                    "channel jkl"
            )
    );

    public List<VimeoVideoDto> fetchVideosByIdList(List<Long> ids) {
        return ids.stream()
                .filter(Objects::nonNull)
                .filter(id -> id > 0)
                .distinct()
                .map(videos::get)
                .collect(toList());
    }
}

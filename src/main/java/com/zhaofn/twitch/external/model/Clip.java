package com.zhaofn.twitch.external.model;

import com.fasterxml.jackson.annotation.JsonProperty;
//这四个是最基本的response结构，我们要把接收到的js model一下，这个只是最基本的一个，而我们需要多个，所以还会有另外一个ClipResponse的record
public record Clip(
        String id,
        String url,
        @JsonProperty("embed_url") String embedUrl,
        @JsonProperty("broadcaster_id") String broadcasterId,
        @JsonProperty("broadcaster_name") String broadcasterName,
        @JsonProperty("creator_id") String creatorId,
        @JsonProperty("creator_name") String creatorName,
        @JsonProperty("video_id") String videoId,
        @JsonProperty("game_id") String gameId,
        String language,
        String title,
        @JsonProperty("view_count") Integer viewCount,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        Float duration,
        @JsonProperty("vod_offset") Integer vodOffset

) {
}

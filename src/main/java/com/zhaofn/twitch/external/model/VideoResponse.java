package com.zhaofn.twitch.external.model;

import java.util.List;

public record VideoResponse (
        List<Video> data
){
}

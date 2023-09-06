package com.zhaofn.twitch.external.model;

import java.util.List;

public record ClipResponse(
        List<Clip> data
) {
}

package com.zhaofn.twitch.external.model;

import com.zhaofn.twitch.db.entity.ItemEntity;

public record FavoriteRequestBody(
        ItemEntity favorite
) {
}
